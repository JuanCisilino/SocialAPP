package com.frost.socialmediaapp.model

import android.net.Uri
import android.os.Parcelable
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.parcel.Parcelize

@Parcelize
open class UserData(
    var name: String?= null,
    var email: String?= null,
    var phone: String?= null,
    var photo: Uri?= null
): Comparable<UserData>, Parcelable{

    fun convert(user:FirebaseUser): UserData{
        this.name = user.displayName
        this.email = user.email
        this.phone = user.phoneNumber
        this.photo = user.photoUrl
        return this
    }

    override fun compareTo(other: UserData): Int {
        return 1
    }
}
