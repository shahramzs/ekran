package com.example.ekran.dataSource.commentDataSource

import com.example.ekran.model.Comment
import com.example.ekran.model.CommentNumber
import com.example.ekran.model.ResponseMessage
import io.reactivex.Single

interface CommentDataSource {
    fun getCommentNumber(code:String?): Single<CommentNumber>
    fun addComment(user:String,token:String,code:String,comment:String):Single<ResponseMessage>
    fun getComment(code:String):Single<List<Comment>>
}