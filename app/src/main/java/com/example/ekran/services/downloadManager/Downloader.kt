package com.example.ekran.services.downloadManager

import android.content.Context

interface Downloader {
    fun download(url:String, context: Context):Long
}