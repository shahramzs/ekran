package com.example.ekran.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Comment(
    val id:Int,
    val user:String,
    val code:String,
    val verify:String,
    val token:String,
    val comment:String,
    val time:String,
    val image:String?
) : Parcelable
