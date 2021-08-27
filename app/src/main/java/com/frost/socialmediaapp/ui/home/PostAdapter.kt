package com.frost.socialmediaapp.ui.home

import android.app.Dialog
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.frost.socialmediaapp.R
import com.frost.socialmediaapp.databinding.ItemPostBinding
import com.frost.socialmediaapp.entities.Post
import com.squareup.picasso.Picasso

class PostAdapter(private val posts:List<Post>, val context: Context): RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    private val postDialog = Dialog(context)

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
            binding.postLinearLayout.setOnClickListener { loadDialog(post) }
        }
    }

    private fun loadDialog(post: Post) {
        postDialog.setContentView(R.layout.item_post_selected)
        postDialog.window?.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        val close = postDialog.findViewById<TextView>(R.id.closeTextView)
        close.setOnClickListener { postDialog.dismiss() }
        val name = postDialog.findViewById<TextView>(R.id.userNameTextView)
        val image = postDialog.findViewById<ImageView>(R.id.profileImageView)
        val date = postDialog.findViewById<TextView>(R.id.dateTextView)
        val description = postDialog.findViewById<TextView>(R.id.descriptionTextView)
        val postImage = postDialog.findViewById<ImageView>(R.id.image)
        name.text = post.userName
        date.text = post.date
        description.text = post.description
        Picasso.get().load(Uri.parse(post.userImage)).into(image)
        Picasso.get().load(Uri.parse(post.image)).into(postImage)
        postDialog.show()
    }
}