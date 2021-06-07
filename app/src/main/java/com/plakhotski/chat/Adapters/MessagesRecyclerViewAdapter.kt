package com.plakhotski.chat.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.plakhotski.chat.Models.Message
import com.plakhotski.chat.R

class MessagesRecyclerViewAdapter(val messages: MutableList<Message>): RecyclerView.Adapter<MessagesRecyclerViewAdapter.ViewHolder>()
{
	private val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
	/**
	 * Provide a reference to the type of views that you are using
	 * (custom ViewHolder).
	 */
	class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
	{
		val leftRoot = view.findViewById<ConstraintLayout>(R.id.leftRoot_messageItem)
		val leftMessageTextView = view.findViewById<TextView>(R.id.leftMessage_messageItem)
		val leftTimestampTextView = view.findViewById<TextView>(R.id.leftTimeStamp_messageItem)

		val rightRoot = view.findViewById<ConstraintLayout>(R.id.rightRoot_messageItem)
		val rightMessageTextView = view.findViewById<TextView>(R.id.rightMessage_messageItem)
		val rightTimestampTextView = view.findViewById<TextView>(R.id.rightTimeStamp_messageItem)
		init
		{
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
	{
		// Create a new view, which defines the UI of the list item
		val view = LayoutInflater.from(parent.context).inflate(R.layout.message_item, parent, false)
		return MessagesRecyclerViewAdapter.ViewHolder(view)
	}

	override fun getItemCount(): Int
	{
		return messages.size
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int)
	{
		holder.leftMessageTextView.text = messages[position].messageText
		holder.leftTimestampTextView.text = messages[position].timeStamp

		if (messages[position].senderUid == currentUserUid)
		{
			holder.rightRoot.visibility = View.VISIBLE
			holder.leftRoot.visibility = View.GONE
			holder.rightMessageTextView.text = messages[position].messageText
			holder.rightTimestampTextView.text = messages[position].timeStamp
		}
		else
		{
			holder.leftRoot.visibility = View.VISIBLE
			holder.rightRoot.visibility = View.GONE
			holder.leftMessageTextView.text = messages[position].messageText
			holder.leftTimestampTextView.text = messages[position].timeStamp
		}
	}
}