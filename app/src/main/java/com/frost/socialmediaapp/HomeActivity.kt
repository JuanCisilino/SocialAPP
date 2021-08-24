package com.frost.socialmediaapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.frost.socialmediaapp.databinding.ActivityHomeBinding
import com.frost.socialmediaapp.model.UserData

class HomeActivity : AppCompatActivity() {

    private val homeViewModel by lazy { ViewModelProvider(this).get(HomeViewModel::class.java) }
    private lateinit var binding: ActivityHomeBinding

    companion object{
        private const val userKey = "UserKey"
        fun start(activity: Activity, user: UserData){
            val intent = Intent(activity, HomeActivity::class.java).apply {
                this.putExtra(userKey, user)
            }
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
     binding = ActivityHomeBinding.inflate(layoutInflater)
        homeViewModel.setUserData(intent.getParcelableExtra(userKey)!!)
     setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_home)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_home, R.id.navigation_profile, R.id.navigation_contacts))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}