package com.example.ekran.feature.subscription.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ekran.R
import com.example.ekran.customView.EkranImageView
import com.example.ekran.model.Subscription
import com.example.ekran.services.imageLoadingService.ImageLoadingService
import com.google.android.material.button.MaterialButton

class SubscriptionAdapter(val imageLoadingService: ImageLoadingService): RecyclerView.Adapter<SubscriptionAdapter.ViewHolder>() {

     var subscribeList = ArrayList<Subscription>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var subscriptionOnClickListener:SubscriptionOnClickListener ?= null

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val user = itemView.findViewById<TextView>(R.id.txtSubscribeUser)
        private val time = itemView.findViewById<TextView>(R.id.txtSubscribeTime)
        private val avatar = itemView.findViewById<EkranImageView>(R.id.avatarSubscribe)
        private val btnDelete = itemView.findViewById<MaterialButton>(R.id.btnDeleteSubscriber)

        fun bind(subscribe:Subscription){
            user.text = subscribe.subscriberUser
            time.text = subscribe.time
            imageLoadingService.load(avatar,com.example.ekran.services.http.Api.ROOT_URL2 + subscribe.image)
            btnDelete.setOnClickListener {
                subscriptionOnClickListener?.deleteSubscription(subscribe)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.subscription_list,parent,false))
    }

    override fun getItemCount(): Int  = subscribeList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(subscribeList[position])

    interface SubscriptionOnClickListener{
        fun deleteSubscription(subscribe:Subscription)
    }

    fun removeSubscription(subscribe:Subscription){
        val index = subscribeList.indexOf(subscribe)
        if(index > -1){
            subscribeList.removeAt(index)
            notifyItemRemoved(index)
        }
    }
}