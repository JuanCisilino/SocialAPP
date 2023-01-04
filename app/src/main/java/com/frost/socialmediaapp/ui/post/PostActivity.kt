package com.frost.socialmediaapp.ui.post


import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.frost.socialmediaapp.R
import com.frost.socialmediaapp.extensions.getDate
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_post.*

class PostActivity : AppCompatActivity(R.layout.activity_post) {

    private val viewModel by lazy { ViewModelProvider(this).get(PostViewModel::class.java) }
    private lateinit var uploadTask: UploadTask
    private lateinit var urltask: Task<Uri>

    companion object{
        fun startActivityForResult(activity: Activity, requestCode: Int){
            activity.startActivityForResult(Intent(activity, PostActivity::class.java), requestCode)
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
            validateAndPush()
        }
    }

    private fun validateAndPush(){
        if (editTextDescription.text.toString().isNotEmpty()){
            viewModel.pushToDb(getDate(), editTextDescription.text.toString(), getUriString())
            Toast.makeText(this,"Uploading", Toast.LENGTH_SHORT).show()
            setResultAndFinish()
        } else {
            Toast.makeText(this,"Must write a description", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setResultAndFinish(){
        val resultIntent = Intent()
        setResult(RESULT_OK, resultIntent)
        finish()
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
    }

    private fun setSelectedImage(uri: Uri?){
        uri?.let{
            val reference = viewModel.repository.storageReference.child(System.currentTimeMillis().toString()+"."+getFileExt(it))
            uploadTask = reference.putFile(it)
            urltask = uploadTask.continueWithTask { reference.downloadUrl }
            Picasso.get().load(it).into(imageView)
            setVisibility()
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