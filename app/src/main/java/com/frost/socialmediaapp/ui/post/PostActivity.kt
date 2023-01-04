package com.frost.socialmediaapp.ui.post


import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import com.frost.socialmediaapp.R
import com.frost.socialmediaapp.extensions.getDate
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_post.*
import java.io.ByteArrayOutputStream
import java.io.File

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
        setCaptureButton()
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

    private fun setCaptureButton() {
        btCamera.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, 100)
        }
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
        when(requestCode){
            100 -> setSelectedBitmap(data?.extras?.get("data") as Bitmap)
            120 -> setSelectedUri(data?.data)
        }
    }

    private fun setSelectedUri(uri: Uri?){
        uri?.let{
            val reference = viewModel.repository.storageReference.child(System.currentTimeMillis().toString()+"."+getFileExt(it))
            uploadTask = reference.putFile(it)
            urltask = uploadTask.continueWithTask { reference.downloadUrl }
            setImage(it)
        }
    }

    private fun setSelectedBitmap(bitmap: Bitmap?) {
        val uri = bitmap?.let { bitmapToUri(it,System.currentTimeMillis().toString()) }
        setSelectedUri(uri)
    }

    private fun setImage(uri: Uri){
        Picasso.get().load(uri).into(imageView)
        setVisibility()
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

    //turn bitmap into uri
    fun bitmapToUri(bitmap: Bitmap, name: String): Uri {

        val file = File(this.cacheDir,name) //Get Access to a local file.
        file.delete() // Delete the File, just in Case, that there was still another File
        file.createNewFile()
        val fileOutputStream = file.outputStream()
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream)
        val bytearray = byteArrayOutputStream.toByteArray()
        fileOutputStream.write(bytearray)
        fileOutputStream.flush()
        fileOutputStream.close()
        byteArrayOutputStream.close()
        return file.toUri()
    }
}