package com.frost.socialmediaapp.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.sql.Timestamp

@Parcelize
data class Post(
    var userName: String?= null,
    var userImage: String?= null,
    var image : String?= null,
    var description: String?= null,
    var date: String?= null,
    var timestamp: Long?=null
    ): Parcelable
