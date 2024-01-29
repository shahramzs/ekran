package com.example.ekran.feature.addVideo

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.ekran.R
import com.example.ekran.base.EkranViewModel
import com.example.ekran.base.TokenContainer
import com.example.ekran.model.EmptyState
import com.example.ekran.model.VideosOnGallery
import com.example.ekran.repository.videosRepository.VideoRepository

class AddVideoFromGalleryViewModel(private val videoRepository: VideoRepository):EkranViewModel() {

     val emptyStateLiveData = MutableLiveData<EmptyState>()
     val videoLiveData = MutableLiveData<List<VideosOnGallery>>()

    private fun getVideo(context:Context){
        if(TokenContainer.token.isNullOrEmpty()){
            emptyStateLiveData.value = EmptyState(true, R.string.empty_state_message,true)
        }else{
            emptyStateLiveData.value = EmptyState(false)
            var list = mutableListOf<VideosOnGallery>()
           list =  videoRepository.getVideoOnGallery(context)
            videoLiveData.value = list
        }
    }

    fun refresh(context:Context){
        getVideo(context)
    }

}