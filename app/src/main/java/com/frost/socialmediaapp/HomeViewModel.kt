package com.frost.socialmediaapp

import androidx.lifecycle.ViewModel
import com.frost.socialmediaapp.entities.UserData

class HomeViewModel: ViewModel() {

    var userData: UserData?= null
    private set

    fun setUserData(user: UserData){
        userData = user
    }
}