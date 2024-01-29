package com.example.ekran.feature.addVideo.uploadVideo

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ekran.base.EXTRA_KEY_DATA
import com.example.ekran.base.EkranViewModel
import com.example.ekran.model.VideoResponse
import com.example.ekran.model.VideosOnGallery
import com.example.ekran.repository.videosRepository.VideoRepository
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.RequestBody

class UploadVideoViewModel(val bundle: Bundle, private val videoRepository: VideoRepository) :
    EkranViewModel() {

    private val _uploadVideoLiveData = MutableLiveData<VideosOnGallery>()

    val uploadVideoLiveData: LiveData<VideosOnGallery>
        get() = _uploadVideoLiveData

    init {
        _uploadVideoLiveData.value = bundle.getParcelable(EXTRA_KEY_DATA)
    }

    fun uploadVideo(token: String, code: String, video: String,requestBody: RequestBody): Single<VideoResponse> {
        return videoRepository.uploadVideo(token, code, video,requestBody)
    }


    fun uploadVideoDetails(
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
    ): Completable {
        return videoRepository.uploadVideoDetails(user,token,thumbnail,code,ip_address,title,description,tag,category,duration,size,status,requestBody)
    }

    fun convertVideo(code:String,token:String,videoUrl:String):Completable{
        return videoRepository.convertVideo(code,token,videoUrl)
    }
}