package com.example.ekran.services.http

import android.content.Context
import net.gotev.uploadservice.protocols.multipart.MultipartUploadRequest


interface ApiServiceUploadVideo {
    fun uploadVideo(context:Context, filePath:String)
}

class CreateApiServiceUploadVideo(): ApiServiceUploadVideo {
    override fun uploadVideo(context: Context, filePath: String) {
        MultipartUploadRequest(context,Api.ROOT_URL)
            .setMethod("POST")
            .addFileToUpload(filePath,"uploadVideo")
            .startUpload()
    }

}