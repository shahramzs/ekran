package com.example.ekran.repository.subscriberRepository

import com.example.ekran.model.DislikeNumber
import com.example.ekran.model.LikeNumber
import com.example.ekran.model.ResponseMessage
import com.example.ekran.model.SubscriberNumber
import com.example.ekran.model.Subscription
import io.reactivex.Completable
import io.reactivex.Single

interface SubscriberRepository {
    fun getSubscriberNumber(subscriberToken:String?,userToken:String?): Single<SubscriberNumber>
    fun subscribe(user:String,token:String,subscriberUser:String,subscriberToken:String):Single<ResponseMessage>
    fun like(user:String,code:String):Single<ResponseMessage>
    fun dislike(user:String,code:String):Single<ResponseMessage>
    fun getLikeNumber(code:String,user:String):Single<LikeNumber>
    fun getDislikeNumber(code:String,user:String):Single<DislikeNumber>
    fun save(user:String,token:String,code:String):Single<ResponseMessage>
    fun getSubscription(token:String):Single<List<Subscription>>
    fun deleteSubscription(token:String): Completable
}