package com.example.ekran.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Subscription(
    val id:Int,
    val subscriberUser:String,
    val subscriberToken:String,
    val image:String,
    val time:String,
) : Parcelable
