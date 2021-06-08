package com.plakhotski.chat

import android.util.Log
import com.google.firebase.database.FirebaseDatabase

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