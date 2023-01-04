package com.frost.socialmediaapp.extensions

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import com.frost.socialmediaapp.R
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

fun Activity.logEventAnalytics(message: String, name:String){
    val analytics = FirebaseAnalytics.getInstance(this)
    val bundle = Bundle()
    bundle.putString("message", message)
    analytics.logEvent(name, bundle)
}

fun Activity.createUser(email: String, pass: String)=
    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pass)

fun Activity.signIn(email: String, pass: String)=
    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, pass)

fun Activity.logOut()= FirebaseAuth.getInstance().signOut()

fun Activity.signInWithCredential(credential: AuthCredential) =
    FirebaseAuth.getInstance().signInWithCredential(credential)

fun Activity.showAlert(){
    val builder = AlertDialog.Builder(this)
    builder.setTitle(getString(R.string.error))
    builder.setMessage(getString(R.string.error_message))
    builder.setPositiveButton(getString(R.string.ok), null)
    val dialog = builder.create()
    dialog.show()
}

fun Activity.getDate(): String {
    val cal = Calendar.getInstance()
    val currentDate = SimpleDateFormat("dd-MMMM-yyyy", Locale.getDefault())
    val date = currentDate.format(cal.time)
    val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    val time = currentTime.format(cal.time)
    return "$date:$time"
}
