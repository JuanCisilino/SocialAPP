package com.frost.socialmediaapp.repositories

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class FirebaseRepo {

    var database = FirebaseDatabase.getInstance()
    var reference = database.getReference("posts")
    var storageReference = FirebaseStorage.getInstance().getReference("Post Images")

}