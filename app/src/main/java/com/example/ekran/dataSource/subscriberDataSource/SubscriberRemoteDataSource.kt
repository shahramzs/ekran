package com.example.ekran.dataSource.subscriberDataSource

import com.example.ekran.model.DislikeNumber
import com.example.ekran.model.LikeNumber
import com.example.ekran.model.ResponseMessage
import com.example.ekran.model.SubscriberNumber
import com.example.ekran.model.Subscription
import com.example.ekran.services.http.ApiService
import com.google.gson.JsonObject
import io.reactivex.Completable
import io.reactivex.Single

class SubscriberRemoteDataSource(private val apiService: ApiService) : SubscriberDataSource {
    override fun getSubscriberNumber(
        subscriberToken: String?,
        userToken: String?
    ): Single<SubscriberNumber> = apiService.getSubscriberNumber(subscriberToken, userToken)

    override fun subscribe(
        user: String,
        token: String,
        subscriberUser: String,
        subscriberToken: String
    ): Single<ResponseMessage> = apiService.subscribe(JsonObject().apply {
        addProperty("user", user)
        addProperty("token", token)
        addProperty("subscriberUser", subscriberUser)
        addProperty("subscriberToken", subscriberToken)
    })

    override fun like(user: String, code: String): Single<ResponseMessage> =
        apiService.like(JsonObject().apply {
            addProperty("user", user)
            addProperty("code", code)
        })

    override fun dislike(user: String, code: String): Single<ResponseMessage> =
        apiService.dislike(JsonObject().apply {
            addProperty("user", user)
            addProperty("code", code)
        })

    override fun getLikeNumber(code: String, user: String): Single<LikeNumber> =
        apiService.getLikeNumber(code, user)

    override fun getDislikeNumber(code: String, user: String): Single<DislikeNumber> =
        apiService.getDislikeNumber(code, user)

    override fun save(user: String, token: String, code: String): Single<ResponseMessage> =
        apiService.save(JsonObject().apply {
            addProperty("user", user)
            addProperty("token", token)
            addProperty("code", code)
        })

    override fun getSubscription(token: String): Single<List<Subscription>> = apiService.getSubscription(token)

    override fun deleteSubscription(token: String): Completable  = apiService.deleteSubscription(token)
}