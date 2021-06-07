package com.plakhotski.chat.Fragments.Controllers

import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.plakhotski.chat.Adapters.MessagesRecyclerViewAdapter
import com.plakhotski.chat.AndroidUtilities.AndroidUtilities
import com.plakhotski.chat.AndroidUtilities.AndroidUtilities.AndroidUtilities.hideKeyboard
import com.plakhotski.chat.FirebaseHelpers
import com.plakhotski.chat.Fragments.ChatFragment
import com.plakhotski.chat.Fragments.ChatFragmentArgs
import com.plakhotski.chat.Models.Chat
import com.plakhotski.chat.Models.Message
import com.plakhotski.chat.R
import com.plakhotski.chat.ViewModels.SharedViewModel
import java.text.SimpleDateFormat
import java.util.*

class ChatController(val chatFragment: ChatFragment): BaseController(chatFragment)
{
    private val opponent = ChatFragmentArgs.fromBundle(chatFragment.requireArguments()).user
    lateinit var currentChatUid: String
    private lateinit var messages: MutableList<Message>

    fun populateFragmentFromArgs()
    {
        view.findViewById<TextView>(R.id.username_fragmentChat).text = opponent.username
    }

    fun sendMessage()
    {
        val multilineText = view.findViewById<EditText>(R.id.multilineText_fragmentChat)
        val message = Message()
        message.chatUid = currentChatUid
        message.messageUid = dbRef.child("users").child(currentUserUid).child("chats").child(message.chatUid!!).child("messages").push().key
        message.senderUid = currentUserUid
        message.messageText = multilineText.text.trim().toString()
        message.timeStamp = SimpleDateFormat("dd.MM.yyyy 'at' HH:mm:ss").format(Date())
        if (message.messageText!!.isNotBlank())
        {
            // Push to currentUser and opponent
            dbRef.child("users").child(currentUserUid).child("chats").child(message.chatUid!!).child("messages").push().setValue(message)
            dbRef.child("users").child(opponent.uid!!).child("chats").child(message.chatUid!!).child("messages").push().setValue(message)
            multilineText.text.clear()
        }
    }

    private fun hookRecyclerViewToMessages()
    {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView_fragmentChat)
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerView.setHasFixedSize(true)
        val a = LinearLayoutManager(view.context)
        dbRef.child("users").child(currentUserUid).child("chats").child(currentChatUid).child("messages").addValueEventListener(object: ValueEventListener{
            override fun onCancelled(error: DatabaseError)
            {
            }
            override fun onDataChange(snapshot: DataSnapshot)
            {
                messages = mutableListOf<Message>()
                snapshot.children.forEach {
                    messages.add(it.getValue(Message::class.java)!!)
                    Log.i(TAG, "message added.")
                }
                recyclerView.adapter = MessagesRecyclerViewAdapter(messages)
                recyclerView.smoothScrollToPosition(messages.size - 1)
            }
        })
    }

    fun liftRecyclerView()
    {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView_fragmentChat)
        recyclerView.smoothScrollToPosition(messages.size - 1)
    }

    fun createChatIfNeeded()
    {
        val currentUserUid = authIns.currentUser?.uid!!

        dbRef.child("users").child(currentUserUid).child("chats").get().addOnSuccessListener {currentUserChats: DataSnapshot ->
            dbRef.child("users").child(opponent.uid!!).child("chats").get().addOnSuccessListener {opponentUserChats: DataSnapshot ->
                val chatUid = dbRef.child("users").child(currentUserUid).child("chats").push().key!!
                val newChat = Chat(chatUid, mutableListOf())
                // If both users have chats that are not null
                if ( currentUserChats.exists() && opponentUserChats.exists())
                {
                    // Compare all chatUids from both users.
                    currentUserChats.children.forEach { currentUserChatsUids: DataSnapshot ->
                        opponentUserChats.children.forEach { opponentChatsUids: DataSnapshot ->
                            if (currentUserChatsUids.key == opponentChatsUids.key)
                            {
                                currentChatUid = currentUserChatsUids.key.toString()
                                Log.i("chats", "chat exists")
                            }
                        }
                    }
                    // If non was found create one
                    if (!this::currentChatUid.isInitialized)
                    {
                        dbRef.child("users").child(currentUserUid).child("chats").child(chatUid).setValue(newChat)
                        dbRef.child("users").child(opponent.uid!!).child("chats").child(chatUid).setValue(newChat)
                        currentChatUid = chatUid
                    }
                }
                else
                {
                    dbRef.child("users").child(currentUserUid).child("chats").child(chatUid).setValue(newChat)
                    dbRef.child("users").child(opponent.uid!!).child("chats").child(chatUid).setValue(newChat)
                    currentChatUid = chatUid
                }
                hookRecyclerViewToMessages()
            }
        }
    }
}