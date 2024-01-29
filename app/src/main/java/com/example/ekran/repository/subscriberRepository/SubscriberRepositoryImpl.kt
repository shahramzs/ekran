package com.example.ekran.repository.subscriberRepository

import com.example.ekran.dataSource.subscriberDataSource.SubscriberDataSource
import com.example.ekran.model.DislikeNumber
import com.example.ekran.model.LikeNumber
import com.example.ekran.model.ResponseMessage
import com.example.ekran.model.SubscriberNumber
import com.example.ekran.model.Subscription
import io.reactivex.Completable
import io.reactivex.Single

class SubscriberRepositoryImpl(private val subscriberRemoteDataSource: SubscriberDataSource):SubscriberRepository {
    override fun getSubscriberNumber(subscriberToken:String?,userToken:String?): Single<SubscriberNumber>  = subscriberRemoteDataSource.getSubscriberNumber(subscriberToken,userToken)
    override fun subscribe(
        user: String,
        token: String,
        subscriberUser: String,
        subscriberToken: String
    ): Single<ResponseMessage> = subscriberRemoteDataSource.subscribe(user,token,subscriberUser,subscriberToken)

    override fun like(user: String, code: String): Single<ResponseMessage> = subscriberRemoteDataSource.like(user,code)

    override fun dislike(user: String, code: String): Single<ResponseMessage>  = subscriberRemoteDataSource.dislike(user,code)

    override fun getLikeNumber(code: String, user: String): Single<LikeNumber>  = subscriberRemoteDataSource.getLikeNumber(code,user)

    override fun getDislikeNumber(code: String, user: String): Single<DislikeNumber>  = subscriberRemoteDataSource.getDislikeNumber(code,user)
    override fun save(user: String, token: String, code: String): Single<ResponseMessage> = subscriberRemoteDataSource.save(user,token,code)
    override fun getSubscription(token: String): Single<List<Subscription>> = subscriberRemoteDataSource.getSubscription(token)

    override fun deleteSubscription(token: String): Completable  = subscriberRemoteDataSource.deleteSubscription(token)
}