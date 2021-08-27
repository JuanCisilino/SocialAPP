package com.frost.socialmediaapp.entities

import android.os.Parcelable
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserData(
    var name: String?= null,
    var email: String?= null,
    var phone: String?= null,
    var photo: String?= null
): Parcelable{

    fun convert(user:FirebaseUser): UserData{
        this.name = user.displayName
        this.email = user.email
        this.phone = user.phoneNumber
        this.photo = user.photoUrl.toString()
        return this
    }

}
