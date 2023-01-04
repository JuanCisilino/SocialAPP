package com.frost.socialmediaapp.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.frost.socialmediaapp.entities.Post
import com.frost.socialmediaapp.repositories.FirebaseRepo
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class HomeFragmentViewModel: ViewModel() {

    val dataLiveData = MutableLiveData<ArrayList<Post>>()
    val errorLiveData = MutableLiveData<Unit>()
    var repository = FirebaseRepo()

    fun getData() {
        val postList = ArrayList<Post>()
        repository.reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children.reversed()){
                    val post = data.getValue(Post::class.java)
                    postList.add(post as Post)
                }
                dataLiveData.postValue(postList)

            }
            override fun onCancelled(error: DatabaseError) {
                errorLiveData.postValue(Unit)
            }

        })
    }
}