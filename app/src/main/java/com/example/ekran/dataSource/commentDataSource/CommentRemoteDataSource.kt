package com.example.ekran.dataSource.commentDataSource

import com.example.ekran.model.Comment
import com.example.ekran.model.CommentNumber
import com.example.ekran.model.ResponseMessage
import com.example.ekran.services.http.ApiService
import com.google.gson.JsonObject
import io.reactivex.Single

class CommentRemoteDataSource(private val apiService: ApiService):CommentDataSource {
    override fun getCommentNumber(code: String?): Single<CommentNumber>  = apiService.getCommentNumber(code)
    override fun addComment(
        user: String,
        token: String,
        code: String,
        comment: String
    ): Single<ResponseMessage>  = apiService.addComment(JsonObject().apply {
        addProperty("user",user)
        addProperty("token",token)
        addProperty("code",code)
        addProperty("comment",comment)
    })

    override fun getComment(code: String): Single<List<Comment>>  = apiService.getComment(code)
}