package com.example.ekran.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AuthResponse(
    val token:String,
    val username:String,
    val email:String,
    val user_id:Int
) : Parcelable
