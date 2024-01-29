package com.example.ekran.services.http

import com.example.ekran.base.TokenContainer
import com.example.ekran.model.AuthResponse
import com.example.ekran.model.Banner
import com.example.ekran.model.Category
import com.example.ekran.model.Comment
import com.example.ekran.model.CommentNumber
import com.example.ekran.model.DislikeNumber
import com.example.ekran.model.LikeNumber
import com.example.ekran.model.ResponseMessage
import com.example.ekran.model.SubscriberNumber
import com.example.ekran.model.Subscription
import com.example.ekran.model.VideoResponse
import com.example.ekran.model.Videos
import com.google.gson.JsonObject
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

interface ApiService {

    @GET(Api.GET_VIDEO)
    fun getVideos(): Single<List<Videos>>

    @GET(Api.GET_Category)
    fun getCategory(): Single<List<Category>>

    @GET(Api.GET_BANNER)
    fun getBanner(): Single<List<Banner>>

    @GET(Api.GET_SUBSCRIBER + "{subscriberToken}" + "/" + "{userToken} ")
    fun getSubscriberNumber(
        @Path("subscriberToken") subscriberToken: String?,
        @Path("userToken") userToken: String?
    ): Single<SubscriberNumber>

    @GET(Api.GET_COMMENT_NUMBER + "{code}")
    fun getCommentNumber(@Path("code") code: String?): Single<CommentNumber>

    @GET(Api.GET_VIDEO_SAME + "{token}" + "/" + "{code}")
    fun getVideoSame(
        @Path("token") token: String?,
        @Path("code") code: String?
    ): Single<List<Videos>>

    @GET(Api.GET_VIDEO_SIMILAR + "{category}" + "/" + "{code}")
    fun getVideoSimilar(
        @Path("category") category: String?,
        @Path("code") code: String?
    ): Single<List<Videos>>

    @Multipart
    @POST(Api.UPLOAD_VIDEO)
    fun uploadVideo(
        @Part("token") token: RequestBody,
        @Part("code") code: RequestBody,
        @Part video: MultipartBody.Part,
    ): Single<VideoResponse>

    @Multipart
    @POST(Api.UPLOAD_VIDEO_DETAILS)
    fun uploadVideoDetails(
        @Part("user") user: RequestBody,
        @Part("token") token: RequestBody,
        @Part thumbnail: MultipartBody.Part,
        @Part("code") code: RequestBody,
        @Part("ip_address") ip_address: RequestBody,
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part("tag") tag: RequestBody,
        @Part("category") category: RequestBody,
        @Part("duration") duration: RequestBody,
        @Part("size") size: RequestBody,
        @Part("status") status: RequestBody,
    ): Completable

    @GET(Api.CONVERT_VIDEO + "{code}" + "/" + "{token}" + "/" + "{videoUrl}")
    fun convertVideo(
        @Path("code") code: String,
        @Path("token") token: String,
        @Path("videoUrl") videoUrl: String
    ): Completable

    @POST(Api.REGISTER)
    fun signUp(@Body jsonObject: JsonObject): Single<ResponseMessage>

    @POST(Api.AUTH)
    fun signIn(@Body jsonObject: JsonObject): Single<AuthResponse>

    @POST(Api.SUBSCRIBE)
    fun subscribe(@Body jsonObject: JsonObject): Single<ResponseMessage>

    @POST(Api.LIKE)
    fun like(@Body jsonObject: JsonObject): Single<ResponseMessage>

    @POST(Api.DISLIKE)
    fun dislike(@Body jsonObject: JsonObject): Single<ResponseMessage>

    @GET(Api.GET_LIKE_NUMBER + "{code}" + "/" + "{user}")
    fun getLikeNumber(
        @Path("code") code: String,
        @Path("user") user: String
    ): Single<LikeNumber>

    @GET(Api.GET_DISLIKE_NUMBER + "{code}" + "/" + "{user}")
    fun getDislikeNumber(
        @Path("code") code: String,
        @Path("user") user: String
    ): Single<DislikeNumber>

    @POST(Api.SAVE)
    fun save(@Body jsonObject: JsonObject):Single<ResponseMessage>

    @POST(Api.ADD_COMMENT)
    fun addComment(@Body jsonObject: JsonObject):Single<ResponseMessage>

    @GET(Api.GET_COMMENT + "{code}" + "/")
    fun getComment(@Path("code") code:String):Single<List<Comment>>

    @GET(Api.GET_SUBSCRIBER_BY_USER + "{token}" + "/")
    fun getSubscription(@Path("token") token:String):Single<List<Subscription>>

    @GET(Api.DELETE_SUBSCRIBER_BY_USER + "{token}" + "/")
    fun deleteSubscription(@Path("token") token:String):Completable
}

fun createApiServiceInstance(): ApiService {

    val okHttpClient = OkHttpClient.Builder()

        .callTimeout(15_000L, TimeUnit.MILLISECONDS)
        .connectTimeout(1500L, TimeUnit.MILLISECONDS)
        .readTimeout(15_000L, TimeUnit.MILLISECONDS)
        .writeTimeout(15_000L, TimeUnit.MILLISECONDS)

        .addInterceptor {
            val oldRequest = it.request()
            val newRequest = oldRequest.newBuilder()
            if (TokenContainer.token != null)
                newRequest.addHeader("Authorization", "Token ${TokenContainer.token}")

            newRequest.addHeader("Accept", "application/json")
            newRequest.method(oldRequest.method, oldRequest.body)
            return@addInterceptor it.proceed(newRequest.build())
        }
        .addInterceptor(HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        })
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl(Api.ROOT_URL)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()
    return retrofit.create(ApiService::class.java)
}