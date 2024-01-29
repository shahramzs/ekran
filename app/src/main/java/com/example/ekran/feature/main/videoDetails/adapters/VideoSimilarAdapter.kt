package com.example.ekran.feature.main.videoDetails.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ekran.R
import com.example.ekran.customView.EkranImageView
import com.example.ekran.model.Videos
import com.example.ekran.services.http.Api
import com.example.ekran.services.imageLoadingService.ImageLoadingService
import com.example.ekran.utils.bytesIntoHumanReadable
import com.example.ekran.utils.implementSpringAnimationTrait

class VideoSimilarAdapter(val imageLoadingService: ImageLoadingService):RecyclerView.Adapter<VideoSimilarAdapter.ViewHolder>() {

    var videos = ArrayList<Videos>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

      var videoSimilarOnClickListener:VideoSimilarOnClickListener ?= null

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val thumbnail = itemView.findViewById<EkranImageView>(R.id.imgSimilarVideo)
        private val title = itemView.findViewById<TextView>(R.id.txtSimilarVideoTitle)
        private val user = itemView.findViewById<TextView>(R.id.txtSimilarVideoUser)
        private val visit = itemView.findViewById<TextView>(R.id.txtSimilarVideoVisit)
        private val time = itemView.findViewById<TextView>(R.id.txtSimilarVideoTime)
        private val length = itemView.findViewById<TextView>(R.id.txtSimilarVideoLength)
        private val duration = itemView.findViewById<TextView>(R.id.txtSimilarVideoDuration)

        fun bind(video:Videos){
            imageLoadingService.load(thumbnail, Api.ROOT_URL2 + video.thumbnail)
            title.text = video.title
            user.text = video.user
            visit.text = video.visit + " بازدید "
            time.text = video.time
            length.text =  bytesIntoHumanReadable(video.size!!.toLong())
            duration.text = video.duration
            itemView.implementSpringAnimationTrait()
            itemView.setOnClickListener {
                videoSimilarOnClickListener?.onVideoSimilarClick(video)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.video_list_similar, parent, false)
        )
    }

    override fun getItemCount(): Int = videos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int)  = holder.bind(videos[position])

    interface VideoSimilarOnClickListener {

        fun onVideoSimilarClick(video: Videos)

    }
}