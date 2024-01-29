package com.example.ekran.feature.main.videoList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ekran.R
import com.example.ekran.customView.EkranImageView
import com.example.ekran.model.Videos
import com.example.ekran.services.http.Api
import com.example.ekran.services.imageLoadingService.ImageLoadingService
import com.example.ekran.utils.implementSpringAnimationTrait
import com.example.ekran.utils.timeFormater


class VideoListAdapter(val imageLoadingService: ImageLoadingService) :
    RecyclerView.Adapter<VideoListAdapter.ViewHolder>() {

    var videoOnClickListener : VideoOnClickListener? = null

    var videos = ArrayList<Videos>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.videos_list, parent, false)
        )
    }

    override fun getItemCount(): Int = videos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bindVideo(videos[position])

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val thumbnail = itemView.findViewById<EkranImageView>(R.id.thumbnail)
        private val videoLength = itemView.findViewById<TextView>(R.id.mainVideoLength)
        private val avatar = itemView.findViewById<EkranImageView>(R.id.userImage)
        private val title = itemView.findViewById<TextView>(R.id.mainVideoTitle)
        private val userTitle = itemView.findViewById<TextView>(R.id.mainUserTitle)
        private val visit = itemView.findViewById<TextView>(R.id.mainViewVideo)
        private val time = itemView.findViewById<TextView>(R.id.mainVideoTime)
        private val moreOption = itemView.findViewById<ImageView>(R.id.btnMoreVideoOption)

        fun bindVideo(video: Videos) {
            imageLoadingService.load(thumbnail, Api.ROOT_URL2 + video.thumbnail)
            imageLoadingService.load(avatar, Api.ROOT_URL2 + video.image)
            videoLength.text = timeFormater(video.duration!!.toLong())
            title.text = video.title
            userTitle.text = video.user
            time.text = video.time
            visit.text = video.visit + " بازدید "
            itemView.implementSpringAnimationTrait()

            itemView.setOnClickListener {
                videoOnClickListener?.onVideoClick(itemView,video)
            }

            moreOption.setOnClickListener {
                videoOnClickListener?.onMoreOptionClick(video)
            }
        }
    }

    interface VideoOnClickListener {

        fun onVideoClick(view:View,video: Videos)
        fun onMoreOptionClick(video: Videos)
    }

}

