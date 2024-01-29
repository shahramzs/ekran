package com.example.ekran.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LikeNumber(
    val like_number:Int,
    val like_status:String
) : Parcelable