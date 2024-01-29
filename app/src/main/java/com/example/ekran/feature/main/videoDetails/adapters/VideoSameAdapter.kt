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

class VideoSameAdapter(val imageLoadingService: ImageLoadingService): RecyclerView.Adapter<VideoSameAdapter.ViewHolder>() {

    var videos = ArrayList<Videos>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

     var videoSameOnClickListener:VideoSameOnClickListener ?= null

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val thumbnail = itemView.findViewById<EkranImageView>(R.id.imgSameVideo)
        private val title = itemView.findViewById<TextView>(R.id.txtSameVideoTitle)
        private val visit = itemView.findViewById<TextView>(R.id.txtSameVideoVisit)
        private val time = itemView.findViewById<TextView>(R.id.txtSameVideoTime)
        private val duration = itemView.findViewById<TextView>(R.id.txtSameVideoDuration)
        private val length = itemView.findViewById<TextView>(R.id.txtSameVideoLength)

        fun bind(video: Videos){
            imageLoadingService.load(thumbnail,Api.ROOT_URL2 + video.thumbnail)
            title.text = video.title
            visit.text = video.visit + " بازدید "
            time.text = video.time
            duration.text = video.duration
            length.text = bytesIntoHumanReadable(video.size!!.toLong())
            itemView.implementSpringAnimationTrait()
            itemView.setOnClickListener {
                videoSameOnClickListener?.onVideoSameClick(video)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.video_list_same, parent, false)
        )
    }

    override fun getItemCount(): Int = videos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(videos[position])

    interface VideoSameOnClickListener {

        fun onVideoSameClick(video: Videos)

    }

}