package com.frost.socialmediaapp.ui.home

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.frost.socialmediaapp.R
import com.frost.socialmediaapp.databinding.ItemPostBinding
import com.frost.socialmediaapp.entities.Post
import com.squareup.picasso.Picasso

class PostAdapter(private val posts:List<Post>): RecyclerView.Adapter<PostAdapter.PostViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return PostViewHolder(layoutInflater.inflate(R.layout.item_post ,parent, false))
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(posts[position])
    }

    override fun getItemCount(): Int = posts.size

    inner class PostViewHolder(view: View):RecyclerView.ViewHolder(view) {

        private var binding = ItemPostBinding.bind(view)

        fun bind(post: Post) {
            binding.userNameTextView.text = post.userName
            binding.dateTextView.text = post.date
            binding.descriptionTextView.text = post.description
            Picasso.get().load(Uri.parse(post.userImage)).into(binding.profileImageView)
            Picasso.get().load(Uri.parse(post.image)).into(binding.image)
        }
    }
}