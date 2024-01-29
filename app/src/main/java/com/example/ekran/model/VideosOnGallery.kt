package com.example.ekran.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VideosOnGallery(
    val id:Long,
    val uri: Uri,
    val name: String,
    val duration: Int,
    val size: Int,
    val resolution:String,
) : Parcelable
