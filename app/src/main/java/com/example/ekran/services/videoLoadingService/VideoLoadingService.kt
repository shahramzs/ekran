package com.example.ekran.services.videoLoadingService

import android.content.Context
import com.example.ekran.customView.EkranVideoView

interface VideoLoadingService {
    fun load(playerView:EkranVideoView,videoUrl:String, context: Context)

    fun defaultLoad(playerView:EkranVideoView,videoUrl:String, context: Context)

    fun loadMovie(playerView:EkranVideoView,videoUrl:String, context: Context)

    fun loadMovie2(playerView:EkranVideoView,videoUrl:String, context: Context)

    fun release()

    fun listener()

    fun analytics()
}