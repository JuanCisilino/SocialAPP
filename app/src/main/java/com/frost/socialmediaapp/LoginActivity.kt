package com.frost.socialmediaapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.frost.socialapp.extensions.showAlert
import com.frost.socialapp.extensions.signInWithCredential
import com.frost.socialmediaapp.entities.UserData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(R.layout.activity_login) {

    private val GOOGLE_SIGN_IN = 100
    private val db = Firebase.firestore

    companion object{
        fun start(context: Context){
            context.startActivity(Intent(context, LoginActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setup()
        session()
    }

    private fun session() {
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val email = prefs.getString(R.string.email.toString(), null)
        email?.let { HomeActivity.start(this) }
    }

    private fun setup() {
        googleButton.setOnClickListener {
            val googleConfig = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            val googleClient = GoogleSignIn.getClient(this, googleConfig)
            googleClient.signOut()
            startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)
        }
    }

    override fun onBackPressed() { }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try{
                val account = task.getResult(ApiException::class.java)
                account?.let {
                    signInWithCredential(GoogleAuthProvider.getCredential(it.idToken, null))
                        .addOnCompleteListener {
                            if (it.isSuccessful){
                                val user = UserData().convert(it.result.user!!)
                                db.collection("users").document(user.email?:"").set(
                                    hashMapOf( "name" to user.name,
                                        "email" to user.email,
                                        "photo_url" to user.photo.toString()))
                                HomeActivity.start(this, user)
                                finish()
                            }else {
                                showAlert()
                            }
                        }
                }
            }catch (e: ApiException){
                showAlert()
            }
        }
    }
}