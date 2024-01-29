package com.example.ekran.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SubscriberNumber(
   val subscribe_number:Int,
    val subscribe_status: String
) : Parcelable
