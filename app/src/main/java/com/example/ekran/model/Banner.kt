package com.example.ekran.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Banner(
    val id:String,
    val banner:String,
    val linkType:Int,
    val linkValue:Int
) : Parcelable
