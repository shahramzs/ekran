package com.example.ekran.dataSource.videoDataSource

import android.content.Context
import com.example.ekran.feature.addVideo.AddVideoFromGallery
import com.example.ekran.model.VideoResponse
import com.example.ekran.model.Videos
import com.example.ekran.model.VideosOnGallery
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.RequestBody

class VideoLocalDataSource:VideoDataSource {
    override fun getVideos(): Single<List<Videos>> {
        TODO("Not yet implemented")
    }

    override fun getFavoritesVideos(): Single<List<Videos>> {
        TODO("Not yet implemented")
    }

    override fun addToFavorites(): Completable {
        TODO("Not yet implemented")
    }

    override fun deleteFromFavorites(): Completable {
        TODO("Not yet implemented")
    }

    override fun getVideoSame(token: String?, code: String?): Single<List<Videos>> {
        TODO("Not yet implemented")
    }

    override fun getVideoSimilar(category: String?, code: String?): Single<List<Videos>> {
        TODO("Not yet implemented")
    }

    override fun getVideoOnGallery(context: Context): MutableList<VideosOnGallery>  = AddVideoFromGallery.addVideo(context)
    override fun uploadVideo(token: String, code: String, video: String,
                             requestBody:RequestBody): Single<VideoResponse> {
        TODO("Not yet implemented")
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
        status:String,
        requestBody: RequestBody
    ): Completable {
        TODO("Not yet implemented")
    }

    override fun convertVideo(code: String, token: String, videoUrl: String):Completable {
        TODO("Not yet implemented")
    }

}