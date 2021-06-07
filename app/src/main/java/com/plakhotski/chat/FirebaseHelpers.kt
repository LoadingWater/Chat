package com.plakhotski.chat

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.plakhotski.chat.Models.Chat
import com.plakhotski.chat.Models.Message
import com.plakhotski.chat.Models.User
import com.plakhotski.chat.ViewModels.SharedViewModel
import java.text.SimpleDateFormat
import java.util.*

class FirebaseHelpers
{
    companion object FirebaseHelpers
    {
        private val TAG = FirebaseHelpers::class.java.simpleName
        fun changeUserOnlineStatus(uid: String, status: Boolean)
        {
            val ref = FirebaseDatabase.getInstance().reference
            ref.child("users").child(uid).child("isOnline").setValue(status).addOnCompleteListener {
                if (it.isSuccessful)
                {
                    Log.i(TAG, "User status changed.")
                }
                else
                {
                    Log.e(TAG, "Failed to change user status.")
                }
            }
        }

        fun addUserToOnlineUserNode(uid: String)
        {
            val ref = FirebaseDatabase.getInstance().reference
            ref.child("offlineUsers").child(uid).removeValue()
            ref.child("onlineUsers").child(uid).setValue(uid).addOnCompleteListener {
                if (it.isSuccessful)
                {
                    Log.i(TAG, "Added online user to the db.")
                }
                else
                {
                    Log.e(TAG, "Failed online user to the db.")
                }
            }
        }
    }
}