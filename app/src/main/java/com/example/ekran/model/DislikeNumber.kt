package com.example.ekran.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DislikeNumber(
    val dislike_number:Int,
    val dislike_status:String
) : Parcelable