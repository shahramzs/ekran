package com.example.ekran.feature.main.videoDetails

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ekran.R
import com.example.ekran.base.EXTRA_KEY_DATA
import com.example.ekran.base.EkranActivity
import com.example.ekran.base.EkranSingleObserver
import com.example.ekran.base.TokenContainer
import com.example.ekran.customView.EkranImageView
import com.example.ekran.customView.EkranVideoView
import com.example.ekran.feature.auth.AuthActivity
import com.example.ekran.feature.main.videoDetails.adapters.CommentAdapter
import com.example.ekran.feature.main.videoDetails.adapters.VideoSameAdapter
import com.example.ekran.feature.main.videoDetails.adapters.VideoSimilarAdapter
import com.example.ekran.model.Comment
import com.example.ekran.model.ResponseMessage
import com.example.ekran.model.Videos
import com.example.ekran.services.downloadManager.Downloader
import com.example.ekran.services.http.Api
import com.example.ekran.services.imageLoadingService.ImageLoadingService
import com.example.ekran.services.videoLoadingService.VideoLoadingService
import com.example.ekran.utils.CommentBottomSheet
import com.example.ekran.utils.convertTime
import com.google.android.material.textfield.TextInputEditText
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber

class VideoDetailsActivity : EkranActivity(), VideoSameAdapter.VideoSameOnClickListener,
    VideoSimilarAdapter.VideoSimilarOnClickListener {

    private val videoDetailsViewModel: VideoDetailsViewModel by viewModel { parametersOf(intent.extras) }

    private val imageLoadingService: ImageLoadingService by inject()
    private val videoLoadingService: VideoLoadingService by inject()
    private val videoSameAdapter: VideoSameAdapter by inject()
    private val videoSimilarAdapter: VideoSimilarAdapter by inject()
    private val downloadVideo:Downloader by inject()
    private val commentAdapter:CommentAdapter by inject()
    val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_details)

        var subStatus: Boolean = false
        var descriptionStatus: Boolean = false
        val player = findViewById<EkranVideoView>(R.id.player)
        val title = findViewById<TextView>(R.id.titleVideoDetailsTV)
        val btnMoreDescription = findViewById<Button>(R.id.btnMoreDescription)
        val time = findViewById<TextView>(R.id.publishedTimeVideoDetailsTV)
        val visit = findViewById<TextView>(R.id.visitVideoDetailsTV)
        val description = findViewById<TextView>(R.id.moreDescriptionTV)
        val like = findViewById<TextView>(R.id.txtLikeVideo)
        val dislike = findViewById<TextView>(R.id.txtDislikeVideo)
        val avatar = findViewById<EkranImageView>(R.id.imgProfileImageSubscribe)
        val username = findViewById<TextView>(R.id.txtSubscriberUsername)
        val subscriberNumber = findViewById<TextView>(R.id.txtSubscriberNumber)
        val btnSubscriber = findViewById<Button>(R.id.btnSubscriber)
        val commentNumber = findViewById<TextView>(R.id.txtCommentNumber)
        val editTextComment = findViewById<TextInputEditText>(R.id.txtAddComment)
        val btnAddComment = findViewById<ImageView>(R.id.btnAddComment)
        val avatarComment = findViewById<EkranImageView>(R.id.imgUserProfile)
        val sameVideo = findViewById<RecyclerView>(R.id.recyclerViewSameVideo)
        val similarVideo = findViewById<RecyclerView>(R.id.recyclerViewSimilarVideo)
        val viewComment = findViewById<TextView>(R.id.txtViewComment)
        val moreDescriptionLayout = findViewById<LinearLayout>(R.id.moreDescriptionLayout)
        val txtCatUser = findViewById<TextView>(R.id.txtCatUser)
        val txtCat = findViewById<TextView>(R.id.txtCat)
        var subscriberUser: String? = null
        var subscriberToken: String? = null
        var user: String? = null
        var code: String? = null
        val btnLike = findViewById<Button>(R.id.btnLikeVideo)
        val btnDislike = findViewById<Button>(R.id.btnDislikeVideo)
        val btnSave = findViewById<Button>(R.id.btnSaveVideo)
        val btnDownload=findViewById<Button>(R.id.btnDownloadVideo)
        var url:String?=null



        sameVideo.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        similarVideo.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        sameVideo.adapter = videoSameAdapter
        similarVideo.adapter = videoSimilarAdapter

        videoSameAdapter.videoSameOnClickListener = this
        videoSimilarAdapter.videoSimilarOnClickListener = this

        videoDetailsViewModel.videoLiveData.observe(this) {
            Timber.tag("videoDetails").d(it.toString())

            val video = it.video!!.split(".")[0] + ".mpd"
            url = it.video
            videoLoadingService.load(player, Api.ROOT_URL2 + video, this)
            imageLoadingService.load(avatar, Api.ROOT_URL2 + it.image)
            title.text = it.title
            time.text = convertTime(it.time)
            visit.text = it.visit + " بازدید "
            like.text = if (it.like.isNullOrEmpty()) "0" else it.like
            dislike.text = if (it.dislike.isNullOrEmpty()) "0" else it.dislike
            username.text = it.user
            imageLoadingService.load(avatarComment, Api.ROOT_URL2 + it.image)
            description.text = it.description
            txtCatUser.text = it.user
            txtCat.text = it.category
            subscriberUser = it.user
            subscriberToken = it.token
            code = it.code

            btnMoreDescription.setOnClickListener {
                if (!descriptionStatus) {
                    descriptionStatus = true
                    moreDescriptionLayout.visibility = VISIBLE
                } else {
                    descriptionStatus = false
                    moreDescriptionLayout.visibility = GONE
                }
            }

        }

        videoDetailsViewModel.likeNumberLiveData.observe(this) {
            Timber.tag("like").d(it.toString())
            like.text = it.like_number.toString()
        }

        videoDetailsViewModel.dislikeLiveData.observe(this) {
            Timber.tag("dislike").d(it.toString())
            dislike.text = it.dislike_number.toString()
        }

        btnLike.setOnClickListener {
            videoDetailsViewModel.like(TokenContainer.username!!,code!!).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object: EkranSingleObserver<ResponseMessage>(compositeDisposable){
                    override fun onSuccess(t: ResponseMessage) {
                        Toast.makeText(this@VideoDetailsActivity,t.toString(),Toast.LENGTH_LONG).show()
                    }

                })
        }

        btnDislike.setOnClickListener {
                videoDetailsViewModel.dislike(TokenContainer.username!!,code!!).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object:EkranSingleObserver<ResponseMessage>(compositeDisposable){
                        override fun onSuccess(t: ResponseMessage) {
                            Toast.makeText(this@VideoDetailsActivity,t.toString(),Toast.LENGTH_LONG).show()
                        }

                    })
        }

        btnSubscriber.setOnClickListener {
            videoDetailsViewModel.subscribe(
                TokenContainer.username!!,
                TokenContainer.token!!,
                subscriberUser!!,
                subscriberToken!!
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : EkranSingleObserver<ResponseMessage>(compositeDisposable) {
                    override fun onSuccess(t: ResponseMessage) {
                        if (!subStatus) {
                            subStatus = true
                            Toast.makeText(
                                this@VideoDetailsActivity,
                                t.toString(),
                                Toast.LENGTH_LONG
                            ).show()
                            btnSubscriber.text = "Unsubscribe"
                            btnSubscriber.setBackgroundColor(Color.rgb(128, 128, 128))
                            btnSubscriber.setTextColor(Color.rgb(0, 0, 0))
                        } else {
                            subStatus = false
                            btnSubscriber.text = "Subscribe"
                            btnSubscriber.setBackgroundColor(Color.rgb(156, 65, 70))
                            btnSubscriber.setTextColor(Color.rgb(255, 255, 255))
                        }
                    }

                })
        }

        btnSave.setOnClickListener {
            videoDetailsViewModel.save(TokenContainer.username!!,TokenContainer.token!!,code!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object:EkranSingleObserver<ResponseMessage>(compositeDisposable){
                    override fun onSuccess(t: ResponseMessage) {
                        Toast.makeText(this@VideoDetailsActivity,t.toString(),Toast.LENGTH_LONG).show()
                    }

                })
        }

        btnDownload.setOnClickListener {
            downloadVideo.download(Api.ROOT_URL2 + url!!,this)
        }

        btnAddComment.setOnClickListener{
            if(editTextComment.text.toString().isNullOrEmpty()){
                Toast.makeText(this,"لطفا متن کامنت خود را وارد نمایید.",Toast.LENGTH_LONG).show()
            }else{
                if(!TokenContainer.token.isNullOrEmpty()){
                    videoDetailsViewModel.addComment(TokenContainer.username!!,TokenContainer.token!!,code!!,editTextComment.text.toString())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object:EkranSingleObserver<ResponseMessage>(compositeDisposable){
                            override fun onSuccess(t: ResponseMessage) {
                                Toast.makeText(this@VideoDetailsActivity,t.toString(),Toast.LENGTH_LONG).show()
                            }

                        })
                }else{
                    Toast.makeText(this,"لطفا از قسمت ورپد به سیستم استفاده کنید یا ثبت نام نمایید.",Toast.LENGTH_LONG).show()
                    startActivity(Intent(this,AuthActivity::class.java))
                }
            }
        }

        videoDetailsViewModel.subscriberNumberLiveData.observe(this) {
            Timber.tag("subscriberNumber").d(it.toString())
            subscriberNumber.text = it.subscribe_number.toString() + " دنبال کننده "
            if (it.subscribe_status == "true") {
                subStatus = true
                btnSubscriber.text = "Unsubscribe"
                btnSubscriber.setBackgroundColor(Color.rgb(128, 128, 128))
                btnSubscriber.setTextColor(Color.rgb(0, 0, 0))
            } else {
                subStatus = false
                btnSubscriber.text = "Subscribe"
                btnSubscriber.setBackgroundColor(Color.rgb(156, 65, 70))
                btnSubscriber.setTextColor(Color.rgb(255, 255, 255))
            }
        }

        videoDetailsViewModel.commentNumberLiveData.observe(this) {
            commentNumber.text = it.comment_number
            if(!it.comment_number.isNullOrEmpty()){
                viewComment.visibility = VISIBLE
            }else{
                viewComment.visibility = GONE
            }
        }

        viewComment.setOnClickListener{
            videoDetailsViewModel.commentLiveData.observe(this){
                Timber.tag("comment").d(it.toString())
                val commentBottomSheet = CommentBottomSheet(this,it as ArrayList<Comment>)
                commentBottomSheet.show(supportFragmentManager, CommentBottomSheet.TAG)
            }
        }

        videoDetailsViewModel.videoSame.observe(this) {
            Timber.tag("videoSame").d(it.toString())
            videoSameAdapter.videos = it as ArrayList<Videos>
        }

        videoDetailsViewModel.videoSimilar.observe(this) {
            Timber.tag("videoSimilar").d(it.toString())
            videoSimilarAdapter.videos = it as ArrayList<Videos>
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        videoLoadingService.release()
    }

    override fun onVideoSameClick(video: Videos) {
        startActivity(Intent(this, VideoDetailsActivity::class.java).apply {
            putExtra(EXTRA_KEY_DATA, video)
        })
    }

    override fun onVideoSimilarClick(video: Videos) {
        startActivity(Intent(this, VideoDetailsActivity::class.java).apply {
            putExtra(EXTRA_KEY_DATA, video)
        })
    }
}