package com.example.ekran.feature.main.videoDetails.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ekran.R
import com.example.ekran.customView.EkranImageView
import com.example.ekran.model.Comment
import com.example.ekran.services.http.Api
import com.example.ekran.services.imageLoadingService.ImageLoadingService

class CommentAdapter(val imageLoadingService: ImageLoadingService): RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

     var comments = ArrayList<Comment>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val image = itemView.findViewById<EkranImageView>(R.id.imgCommentProfile)
        private val user = itemView.findViewById<TextView>(R.id.txtCommentUser)
        private val comment = itemView.findViewById<TextView>(R.id.txtCommentText)
        private val time = itemView.findViewById<TextView>(R.id.txtCommentTime)

        fun bind(c:Comment){

            imageLoadingService.load(image, Api.ROOT_URL2 + c.image)
            user.text = c.user
            comment.text = c.comment
            time.text = c.time
        }
    }

     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
         return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.comment_list,parent,false))
     }

     override fun getItemCount(): Int = comments.size

     override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(comments[position])
 }