package com.plakhotski.chat.Fragments.Controllers

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.plakhotski.chat.Adapters.OnlineUsersRecyclerAdapter
import com.plakhotski.chat.Fragments.OnlineUsersFragment
import com.plakhotski.chat.Models.User
import com.plakhotski.chat.R
import java.util.*


class OnlineUsersController(val onlineUsersFragment: OnlineUsersFragment) : BaseController(onlineUsersFragment)
{
	private val allUsers = mutableListOf<User>()

	fun setUpHandleForUserDisconnection()
	{
		val uid = authIns.currentUser!!.uid
		dbRef.child("users").child(uid).child("isOnline").onDisconnect().setValue("false")
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
				allUsers.clear()
				snapshot.children.forEach {
					val user = it.getValue(User::class.java)!!
					if (authIns.currentUser?.uid!! != user.uid)
					{
						allUsers.add(user)
					}
				}
				recyclerView.layoutManager = LinearLayoutManager(view.context)
				recyclerView.setHasFixedSize(true)
				recyclerView.adapter = OnlineUsersRecyclerAdapter(allUsers)
			}
		})
	}

	fun filterUsers(text: String)
	{
		val temp: MutableList<User> = mutableListOf()
		for (user in allUsers)
		{
			if (user.username!!.toLowerCase(Locale.ROOT).contains(text))
			{
				temp.add(user)
			}
		}
		val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView_fragmentOnlineUsers)
		recyclerView.adapter = OnlineUsersRecyclerAdapter(temp)
		(recyclerView.adapter as OnlineUsersRecyclerAdapter).notifyDataSetChanged()
	}
}