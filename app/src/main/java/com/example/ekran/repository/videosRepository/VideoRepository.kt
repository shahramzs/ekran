package com.example.ekran.repository.videosRepository

import android.content.Context
import com.example.ekran.model.VideoResponse
import com.example.ekran.model.Videos
import com.example.ekran.model.VideosOnGallery
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.RequestBody

interface VideoRepository {

    fun getVideos(): Single<List<Videos>>

    fun getFavoritesVideos(): Single<List<Videos>>

    fun addToFavorites(): Completable

    fun deleteFromFavorites(): Completable

    fun getVideoSame(token: String?, code: String?): Single<List<Videos>>

    fun getVideoSimilar(category: String?, code: String?): Single<List<Videos>>

    fun getVideoOnGallery(context: Context): MutableList<VideosOnGallery>

    fun uploadVideo(token: String, code: String, video: String,requestBody: RequestBody): Single<VideoResponse>


    fun uploadVideoDetails(
        user: String,
        token: String,
        thumbnail: String,
        code: String,
        ip_address: String,
        title: String,
        description: String,
        tag:String,
        category:String,
        duration:String,
        size:String,
        status:String,
        requestBody:RequestBody
    ): Completable

    fun convertVideo(code:String,token:String,videoUrl:String):Completable

}