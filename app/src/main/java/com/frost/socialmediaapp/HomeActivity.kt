package com.frost.socialmediaapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.frost.socialmediaapp.databinding.ActivityHomeBinding
import com.frost.socialmediaapp.entities.UserData
import com.frost.socialmediaapp.ui.home.HomeFragment
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeActivity : AppCompatActivity() {

    private val homeViewModel by lazy { ViewModelProvider(this).get(HomeViewModel::class.java) }
    private lateinit var binding: ActivityHomeBinding
    private val db = Firebase.firestore

    companion object{
        private const val userKey = "UserKey"
        fun start(activity: Activity, user: UserData?= null){
            val intent = Intent(activity, HomeActivity::class.java).apply {
                this.putExtra(userKey, user)
            }
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        homeViewModel.setUserData(intent.getParcelableExtra(userKey)?: retrieveUser())
        getDataFromFirestore()
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_home)
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_home, R.id.navigation_profile, R.id.navigation_contacts))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        saveData()
    }

    private fun getDataFromFirestore() {
        db.collection("users").document(homeViewModel.userData?.email!!).get()
            .addOnSuccessListener { homeViewModel.userData?.phone = it.get("phone") as String? }
    }

    override fun onBackPressed() { }

    private fun retrieveUser(): UserData {
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val userData = UserData()
        userData.name = prefs.getString(R.string.name.toString(), null)
        userData.email = prefs.getString(R.string.email.toString(), null)
        userData.phone = prefs.getString(R.string.phone.toString(), null)
        userData.photo = Uri.parse(prefs.getString(R.string.photo.toString(), null))
        return userData
    }

    private fun saveData() {
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.putString(R.string.email.toString(), homeViewModel.userData?.email)
        prefs.putString(R.string.phone.toString(), homeViewModel.userData?.phone)
        prefs.putString(R.string.name.toString(), homeViewModel.userData?.name)
        prefs.putString(R.string.photo.toString(), homeViewModel.userData?.photo.toString())
        prefs.apply()
    }

}