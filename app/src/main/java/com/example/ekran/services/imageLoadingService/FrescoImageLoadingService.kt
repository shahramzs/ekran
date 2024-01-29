package com.example.ekran.services.imageLoadingService

import android.net.Uri
import com.example.ekran.customView.EkranImageView
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.common.ResizeOptions
import com.facebook.imagepipeline.request.ImageRequestBuilder


class FrescoImageLoadingService : ImageLoadingService {
    override fun load(imageView: EkranImageView, imageUrl: String) {
        if (imageView is SimpleDraweeView) {

            val request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(imageUrl))
                .setResizeOptions(ResizeOptions(50, 50))
                .build()
            imageView.controller = Fresco.newDraweeControllerBuilder()
                .setOldController(imageView.controller)
                .setImageRequest(request)
                .build()
            imageView.setImageURI(imageUrl)
        } else {
            throw IllegalStateException("ImageView must be instance of SimpleDraweeView")
        }

    }
}