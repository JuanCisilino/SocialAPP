package com.frost.socialmediaapp.ui.post


import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Toast
import com.frost.socialmediaapp.R
import com.frost.socialmediaapp.entities.Post
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_post.*
import java.text.SimpleDateFormat
import java.util.*

class PostActivity : AppCompatActivity(R.layout.activity_post) {

    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private var user = FirebaseAuth.getInstance().currentUser
    private lateinit var uploadTask: UploadTask
    private lateinit var urltask: Task<Uri>

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
        database = FirebaseDatabase.getInstance()
        reference = database.getReference("posts")
        val post = getPost()
        reference.child("${post.date}").setValue(post)
    }

    private fun getPost(): Post {
        val post = Post()
        post.date = getDate()
        post.userName = user?.displayName
        post.userImage = user?.photoUrl.toString()
        post.description = editTextDescription.text.toString()
        post.image = getUriString()
        return post
    }

    private fun getDate(): String {
        val cal = Calendar.getInstance()
        val currentDate = SimpleDateFormat("dd-MMMM-yyyy", Locale.getDefault())
        val date = currentDate.format(cal.time)
        val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val time = currentTime.format(cal.time)
        return "$date:$time"
    }

    private fun setGalleryButton() {
        btGallery.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 120)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 120) setSelectedImage(data?.data)
        setVisibility()
    }

    private fun setSelectedImage(uri: Uri?){
        storageReference = FirebaseStorage.getInstance().getReference("Post Images")
        uri?.let{
            val reference = storageReference.child(System.currentTimeMillis().toString()+"."+getFileExt(it))
            uploadTask = reference.putFile(it)
            urltask = uploadTask.continueWithTask { reference.downloadUrl }
            Picasso.get().load(it).into(imageView)
        }
    }

    private fun getUriString(): String =
        if (urltask.isComplete and urltask.isSuccessful) urltask.result.toString() else ""

    private fun getFileExt(uri: Uri): String? {
        val contextResolver = contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType((contextResolver.getType(uri)))
    }

    private fun setVisibility() {
        buttonsLayout.visibility = View.GONE
        imageLayout.visibility = View.VISIBLE
        buttonUpload.visibility = View.VISIBLE
    }
}