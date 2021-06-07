package com.plakhotski.chat.Fragments.Controllers

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.plakhotski.chat.Adapters.OnlineUsersRecyclerAdapter
import com.plakhotski.chat.FirebaseHelpers
import com.plakhotski.chat.Fragments.OnlineUsersFragment
import com.plakhotski.chat.Models.User
import com.plakhotski.chat.R
import com.plakhotski.chat.ViewModels.SharedViewModel

class OnlineUsersController(val onlineUsersFragment: OnlineUsersFragment): BaseController(onlineUsersFragment)
{
    fun setUpHandleForUserDisconnection()
    {
        val uid = authIns.currentUser!!.uid
        val offlineUsers = FirebaseDatabase.getInstance().reference.child("offlineUsers").child(uid)
        offlineUsers.onDisconnect().setValue(uid)
        val online = FirebaseDatabase.getInstance().reference.child("onlineUsers").child(uid)
        online.onDisconnect().removeValue()
        FirebaseHelpers.changeUserOnlineStatus(uid, false)
    }

    fun showUsers()
    {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView_fragmentOnlineUsers)
        dbRef.child("users").addValueEventListener(object : ValueEventListener
        {
            override fun onCancelled(error: DatabaseError)
            {
            }
            override fun onDataChange(snapshot: DataSnapshot)
            {
                val uids = mutableListOf<User>()
                snapshot.children.forEach {
                    val user = it.getValue(User::class.java)!!
                    if (authIns.currentUser?.uid!! != user.uid)
                    {
                        uids.add(user)
                    }
                }
                recyclerView.layoutManager = LinearLayoutManager(view.context)
                recyclerView.adapter = OnlineUsersRecyclerAdapter(uids)
            }
        })
    }
}