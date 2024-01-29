package com.example.ekran.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Videos(
    val user:String?,
    val image:String?,
    val title:String?,
    val token:String?,
    val thumbnail:String?,
    val code:String?,
    val duration:String?,
    val size:String?,
    val verify:Boolean?,
    val time:String?,
    val visit:String?,
    val description:String?,
    val tag:String?,
    val category:String?,
    val video:String?,
    var like:String? = "0",
    var dislike:String? = "0",
) : Parcelable



