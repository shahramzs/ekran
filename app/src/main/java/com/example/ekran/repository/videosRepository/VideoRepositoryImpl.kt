package com.example.ekran.repository.videosRepository

import android.content.Context
import com.example.ekran.dataSource.videoDataSource.VideoDataSource
import com.example.ekran.model.ResponseDetail
import com.example.ekran.model.VideoResponse
import com.example.ekran.model.Videos
import com.example.ekran.model.VideosOnGallery
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.RequestBody

class VideoRepositoryImpl(private val videoRemoteDataSource: VideoDataSource, private val videoLocalDataSource: VideoDataSource):VideoRepository {

    override fun getVideos(): Single<List<Videos>>  = videoRemoteDataSource.getVideos()

    override fun getFavoritesVideos(): Single<List<Videos>> {
        TODO("Not yet implemented")
    }

    override fun addToFavorites(): Completable {
        TODO("Not yet implemented")
    }

    override fun deleteFromFavorites(): Completable {
        TODO("Not yet implemented")
    }

    override fun getVideoSame(token: String?, code: String?): Single<List<Videos>> = videoRemoteDataSource.getVideoSame(token,code)

    override fun getVideoSimilar(category: String?, code: String?): Single<List<Videos>> = videoRemoteDataSource.getVideoSimilar(category,code)

    override fun getVideoOnGallery(context: Context):  MutableList<VideosOnGallery>  = videoLocalDataSource.getVideoOnGallery(context)

    override fun uploadVideo(token: String, code: String, video: String,requestBody: RequestBody): Single<VideoResponse>  =  videoRemoteDataSource.uploadVideo(token,code,video,requestBody)


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
        requestBody:RequestBody
    ): Completable  = videoRemoteDataSource.uploadVideoDetails(user,token,thumbnail,code,ip_address,title,description,tag,category,duration,size,status,requestBody)

    override fun convertVideo(code: String, token: String, videoUrl: String):Completable = videoRemoteDataSource.convertVideo(code,token,videoUrl)


}