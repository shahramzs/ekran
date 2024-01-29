package com.example.ekran.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VideoResponse(
    val id:String,
    val token:String,
    val code:String,
    val video:String,
    val verify:String,
    val time:String
) : Parcelable
