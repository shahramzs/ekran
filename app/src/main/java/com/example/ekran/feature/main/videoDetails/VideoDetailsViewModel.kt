package com.example.ekran.feature.main.videoDetails

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ekran.base.EXTRA_KEY_DATA
import com.example.ekran.base.EkranSingleObserver
import com.example.ekran.base.EkranViewModel
import com.example.ekran.base.TokenContainer
import com.example.ekran.model.Comment
import com.example.ekran.model.CommentNumber
import com.example.ekran.model.DislikeNumber
import com.example.ekran.model.LikeNumber
import com.example.ekran.model.ResponseMessage
import com.example.ekran.model.SubscriberNumber
import com.example.ekran.model.Videos
import com.example.ekran.repository.commentRepository.CommentRepository
import com.example.ekran.repository.subscriberRepository.SubscriberRepository
import com.example.ekran.repository.videosRepository.VideoRepository
import com.example.ekran.utils.asyncNetworkRequest
import io.reactivex.Single

class VideoDetailsViewModel(val bundle: Bundle, private val subscriberRepository: SubscriberRepository,
                            private val commentRepository: CommentRepository, private val videoRepository: VideoRepository
) : EkranViewModel() {

    private val _videoLiveData = MutableLiveData<Videos>()
    private val _subscriberNumberLiveData = MutableLiveData<SubscriberNumber>()
    private val _commentNumberLiveData = MutableLiveData<CommentNumber>()
    private val _videoSame = MutableLiveData<List<Videos>>()
    private val _videoSimilar = MutableLiveData<List<Videos>>()
    private val _likeNumberLiveData = MutableLiveData<LikeNumber>()
    private val _dislikeNumberLiveData = MutableLiveData<DislikeNumber>()
    private val _commentLiveData = MutableLiveData<List<Comment>>()

    val videoLiveData : LiveData<Videos>
        get() = _videoLiveData

    val subscriberNumberLiveData:LiveData<SubscriberNumber>
        get() = _subscriberNumberLiveData

    val commentNumberLiveData:LiveData<CommentNumber>
        get() = _commentNumberLiveData

    val videoSame:LiveData<List<Videos>>
        get() = _videoSame

    val videoSimilar:LiveData<List<Videos>>
        get() = _videoSimilar

    val likeNumberLiveData:LiveData<LikeNumber>
        get() = _likeNumberLiveData

    val dislikeLiveData:LiveData<DislikeNumber>
        get() = _dislikeNumberLiveData

    val commentLiveData : LiveData<List<Comment>>
        get() = _commentLiveData

    init{
        _videoLiveData.value = bundle.getParcelable(EXTRA_KEY_DATA)


        subscriberRepository.getSubscriberNumber(_videoLiveData.value!!.token,if(TokenContainer.token.isNullOrEmpty()) "noUser" else TokenContainer.token)
            .asyncNetworkRequest()
            .subscribe(object: EkranSingleObserver<SubscriberNumber>(compositeDisposable){
                override fun onSuccess(t: SubscriberNumber) {
                    _subscriberNumberLiveData.value = t
                }

            })

        commentRepository.getCommentNumber(_videoLiveData.value!!.code)
            .asyncNetworkRequest()
            .subscribe(object: EkranSingleObserver<CommentNumber>(compositeDisposable){
                override fun onSuccess(t: CommentNumber) {
                   _commentNumberLiveData.value = t
                }

            })

        videoRepository.getVideoSame(_videoLiveData.value!!.token, _videoLiveData.value!!.code)
            .asyncNetworkRequest()
            .subscribe(object: EkranSingleObserver<List<Videos>>(compositeDisposable){
                override fun onSuccess(t: List<Videos>) {
                    _videoSame.value = t
                }

            })

        videoRepository.getVideoSimilar(_videoLiveData.value!!.category, _videoLiveData.value!!.code)
            .asyncNetworkRequest()
            .subscribe(object: EkranSingleObserver<List<Videos>>(compositeDisposable){
                override fun onSuccess(t: List<Videos>) {
                    _videoSimilar.value = t
                }

            })

        subscriberRepository.getLikeNumber(_videoLiveData.value!!.code!!,if(TokenContainer.token!!.isEmpty()) "noUser" else TokenContainer.username!!)
            .asyncNetworkRequest()
            .subscribe(object:EkranSingleObserver<LikeNumber>(compositeDisposable){
                override fun onSuccess(t: LikeNumber) {
                    _likeNumberLiveData.value = t
                }

            })

        subscriberRepository.getDislikeNumber(_videoLiveData.value!!.code!!,if(TokenContainer.token!!.isEmpty()) "noUser" else TokenContainer.username!!)
            .asyncNetworkRequest()
            .subscribe(object:EkranSingleObserver<DislikeNumber>(compositeDisposable){
                override fun onSuccess(t: DislikeNumber) {
                    _dislikeNumberLiveData.value = t
                }

            })

        commentRepository.getComment(_videoLiveData.value!!.code!!)
            .asyncNetworkRequest()
            .subscribe(object:EkranSingleObserver<List<Comment>>(compositeDisposable){
                override fun onSuccess(t: List<Comment>) {
                    _commentLiveData.value = t
                }

            })

    }

    fun subscribe(user:String,token:String,subscribeUser:String,subscribeToken:String): Single<ResponseMessage> {
        return subscriberRepository.subscribe(user,token,subscribeUser,subscribeToken)
    }
    fun like(user:String,code:String):Single<ResponseMessage>{
        return subscriberRepository.like(user,code)
    }
    fun dislike(user:String,code:String):Single<ResponseMessage>{
        return subscriberRepository.dislike(user,code)
    }
    fun save(user:String,token:String,code:String):Single<ResponseMessage>{
        return subscriberRepository.save(user,token,code)
    }
    fun addComment(user:String,token:String,code:String,comment:String):Single<ResponseMessage>{
        return commentRepository.addComment(user,token,code,comment)
    }
}