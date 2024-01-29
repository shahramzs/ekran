package com.example.ekran.services.imageLoadingService

import com.example.ekran.customView.EkranImageView

interface ImageLoadingService {
    fun load(imageView: EkranImageView, imageUrl:String)
}