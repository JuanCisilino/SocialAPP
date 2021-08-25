package com.frost.socialmediaapp.ui.post

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import com.frost.socialmediaapp.R
import com.frost.socialmediaapp.entities.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_post.*
import java.text.SimpleDateFormat
import java.util.*

class PostActivity : AppCompatActivity(R.layout.activity_post) {

    private lateinit var database: DatabaseReference
    private var user = FirebaseAuth.getInstance().currentUser
    private var selectedUri = ""

    companion object{
        fun start(context: Context){
            context.startActivity(Intent(context, PostActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setButtons()
    }

    private fun setButtons() {
        setCaptureButton()
        setGalleryButton()
        setUploadButton()
    }

    private fun setUploadButton() {
        buttonUpload.setOnClickListener {
            pushToDb()
            Toast.makeText(this,"Registered", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun pushToDb() {
        database = Firebase.database.reference
        database.child("posts").child(user?.email?:user?.uid?:"").setValue(getPost())
    }

    private fun getPost(): Post {
        val post = Post()
        post.date = getDate()
        post.userName = user?.displayName
        post.userImage = user?.photoUrl.toString()
        post.description = editTextDescription.text.toString()
        post.image = selectedUri
        return post
    }

    private fun getDate(): String {
        val cal = Calendar.getInstance()
        val currentDate = SimpleDateFormat("dd-MMMM-yyyy")
        val date = currentDate.format(cal)
        val currentTime = SimpleDateFormat("HH:mm:ss")
        val time = currentTime.format(cal)
        return "$date:$time"
    }

    private fun setGalleryButton() {
        btGallery.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 120)
        }
    }

    private fun setCaptureButton() {
        btCapture.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.resolveActivity(this.packageManager)?.let {  startActivityForResult(intent, 100) }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            100 -> { setSelectedImage(data?.data) }
            120 -> { setSelectedImage(data?.data) }
        }
        setVisibility()
    }

    private fun setSelectedImage(uri: Uri?){
        selectedUri = uri.toString()
        Picasso.get().load(uri).into(imageView)
    }

    private fun setVisibility() {
        buttonsLayout.visibility = View.GONE
        imageLayout.visibility = View.VISIBLE
        buttonUpload.visibility = View.VISIBLE
    }
}