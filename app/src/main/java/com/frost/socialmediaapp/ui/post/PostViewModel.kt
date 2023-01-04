package com.frost.socialmediaapp.ui.post

import androidx.lifecycle.ViewModel
import com.frost.socialmediaapp.entities.Post
import com.frost.socialmediaapp.repositories.FirebaseRepo
import com.google.firebase.auth.FirebaseAuth
import java.util.*

class PostViewModel: ViewModel() {

    var repository = FirebaseRepo()
    var user = FirebaseAuth.getInstance().currentUser

    fun pushToDb(date: String, description: String, uri: String) {
        val post = getPost(date, description, uri)
        repository.reference.child("${post.date}:${post.userName}".lowercase(Locale.getDefault())).setValue(post)
    }

    fun getPost(date: String, description: String, uri: String): Post {
        val post = Post()
        post.date = date
        post.userName = user?.displayName
        post.userImage = user?.photoUrl.toString()
        post.description = description
        post.image = uri
        post.timestamp = Calendar.getInstance().timeInMillis
        return post
    }
}