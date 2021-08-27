package com.frost.socialmediaapp.ui.contacts

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.frost.socialmediaapp.R
import com.frost.socialmediaapp.databinding.ItemUserBinding
import com.frost.socialmediaapp.entities.UserData
import com.squareup.picasso.Picasso

class UsersAdapter(private val users:List<UserData>, val context: Context): RecyclerView.Adapter<UsersAdapter.UsersViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return UsersViewHolder(layoutInflater.inflate(R.layout.item_user ,parent, false))
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount(): Int = users.size

    inner class UsersViewHolder(view: View): RecyclerView.ViewHolder(view) {

        private var binding = ItemUserBinding.bind(view)

        fun bind(user: UserData) {
            binding.userNameTextView.text = user.name
            binding.emailTextView.text = user.email
            Picasso.get().load(Uri.parse(user.photo)).into(binding.profileImageView)
        }
    }

}