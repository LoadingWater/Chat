package com.plakhotski.chat.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.plakhotski.chat.FirebaseHelpers
import com.plakhotski.chat.Fragments.OnlineUsersFragmentDirections
import com.plakhotski.chat.Models.User
import com.plakhotski.chat.R
import com.plakhotski.chat.ViewModels.SharedViewModel


class OnlineUsersRecyclerAdapter(private val users: MutableList<User>): RecyclerView.Adapter<OnlineUsersRecyclerAdapter.ViewHolder>()
{

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        lateinit var opponent: User
        val username: TextView = view.findViewById(R.id.username_userItem)
        val startChat: ImageButton = view.findViewById(R.id.startChat_userItem)
        init
        {
            // Define click listener for the ViewHolder's View.
            startChat.setOnClickListener {
                val action = OnlineUsersFragmentDirections.actionOnlineUsersFragmentToChatFragment(opponent)
                Navigation.findNavController(view).navigate(action)
            }
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder
    {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.user_item, viewGroup, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int)
    {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.opponent = users[position]
        viewHolder.username.text = users[position].username
        // Setting status drawable
        val statusImage = if (users[position].isOnline == true) viewHolder.username.context.getDrawable(R.drawable.ic_baseline_circle_24_green) else viewHolder.username.context.getDrawable(R.drawable.ic_baseline_circle_24_red)
        viewHolder.username.setCompoundDrawablesWithIntrinsicBounds(null, null, statusImage, null)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = users.size

}
