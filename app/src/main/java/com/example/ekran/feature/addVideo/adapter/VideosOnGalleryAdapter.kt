package com.example.ekran.feature.addVideo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ekran.R
import com.example.ekran.customView.EkranImageView
import com.example.ekran.model.VideosOnGallery
import com.example.ekran.services.imageLoadingService.ImageLoadingService
import com.example.ekran.utils.bytesIntoHumanReadable
import com.example.ekran.utils.implementSpringAnimationTrait
import com.example.ekran.utils.timeFormater
import com.google.android.material.button.MaterialButton

class VideosOnGalleryAdapter(val imageLoadingService: ImageLoadingService): RecyclerView.Adapter<VideosOnGalleryAdapter.ViewHolder>() {

    var videosOnGallery = ArrayList<VideosOnGallery>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private var selectedItems = 0

    var videoOnClickListener:VideoOnClickListener ?= null

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val thumbnail = itemView.findViewById<EkranImageView>(R.id.imageVide_thumbnail)
        private val duration = itemView.findViewById<TextView>(R.id.add_video_duration)
        private val size = itemView.findViewById<TextView>(R.id.add_video_size)
        private val btnSubmit = itemView.findViewById<MaterialButton>(R.id.add_video_submit)

        fun bind(videosOnGallery: VideosOnGallery){
            imageLoadingService.load(thumbnail,videosOnGallery.uri.toString())
            duration.text = timeFormater(videosOnGallery.duration.toLong())
            size.text = bytesIntoHumanReadable(videosOnGallery.size.toLong())

            if(selectedItems == absoluteAdapterPosition){
                btnSubmit.visibility = View.VISIBLE
            }else{
                btnSubmit.visibility = View.GONE
            }

            itemView.implementSpringAnimationTrait()
            itemView.setOnClickListener {
                videoOnClickListener?.videoClick(videosOnGallery)
                selectedItems = absoluteAdapterPosition
                btnSubmit.visibility = View.VISIBLE
                notifyDataSetChanged()
            }

            btnSubmit.setOnClickListener{
                videoOnClickListener?.btnSubmit(videosOnGallery)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_videos,parent,false))
    }

    override fun getItemCount(): Int  = videosOnGallery.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int)  = holder.bind(videosOnGallery[position])

    interface VideoOnClickListener{
        fun videoClick(videosOnGallery: VideosOnGallery)

        fun btnSubmit(videosOnGallery: VideosOnGallery)
    }
}