package com.example.ekran.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CommentNumber(
    val comment_number:String
) : Parcelable
