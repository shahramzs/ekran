package com.example.ekran.dataSource.videoDataSource

import android.content.Context
import com.example.ekran.model.VideoResponse
import com.example.ekran.model.Videos
import com.example.ekran.model.VideosOnGallery
import com.example.ekran.services.http.ApiService
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class VideoRemoteDataSource(private val apiService: ApiService) : VideoDataSource {

    override fun getVideos(): Single<List<Videos>> = apiService.getVideos()

    override fun getFavoritesVideos(): Single<List<Videos>> {
        TODO("Not yet implemented")
    }

    override fun addToFavorites(): Completable {
        TODO("Not yet implemented")
    }

    override fun deleteFromFavorites(): Completable {
        TODO("Not yet implemented")
    }

    override fun getVideoSame(token: String?, code: String?): Single<List<Videos>> =
        apiService.getVideoSame(token, code)

    override fun getVideoSimilar(category: String?, code: String?): Single<List<Videos>> =
        apiService.getVideoSimilar(category, code)

    override fun getVideoOnGallery(context: Context): MutableList<VideosOnGallery> {
        TODO("Not yet implemented")
    }

    override fun uploadVideo(token: String, code: String, video: String,requestBody:RequestBody): Single<VideoResponse> {
        return apiService.uploadVideo(
            token.toRequestBody("text/plain".toMediaTypeOrNull()),
            code.toRequestBody("text/plain".toMediaTypeOrNull()),
            MultipartBody.Part.createFormData("video", video,requestBody),
        )
    }

    override fun uploadVideoDetails(
        user: String,
        token: String,
        thumbnail: String,
        code: String,
        ip_address: String,
        title: String,
        description: String,
        tag: String,
        category: String,
        duration: String,
        size: String,
        status: String,
        requestBody:RequestBody
    ): Completable {
        return apiService.uploadVideoDetails(
            user.toRequestBody("text/plain".toMediaTypeOrNull()),
            token.toRequestBody("text/plain".toMediaTypeOrNull()),
            MultipartBody.Part.createFormData("thumbnail", thumbnail,requestBody),
            code.toRequestBody("text/plain".toMediaTypeOrNull()),
            ip_address.toRequestBody("text/plain".toMediaTypeOrNull()),
            title.toRequestBody("text/plain".toMediaTypeOrNull()),
            description.toRequestBody("text/plain".toMediaTypeOrNull()),
            tag.toRequestBody("text/plain".toMediaTypeOrNull()),
            category.toRequestBody("text/plain".toMediaTypeOrNull()),
            duration.toRequestBody("text/plain".toMediaTypeOrNull()),
            size.toRequestBody("text/plain".toMediaTypeOrNull()),
            status.toRequestBody("text/plain".toMediaTypeOrNull()),
        )
    }

    override fun convertVideo(code: String, token: String, videoUrl: String):Completable = apiService.convertVideo(code,token,videoUrl)



}