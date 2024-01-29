package com.example.ekran.repository.commentRepository

import com.example.ekran.dataSource.commentDataSource.CommentDataSource
import com.example.ekran.model.Comment
import com.example.ekran.model.CommentNumber
import com.example.ekran.model.ResponseMessage
import io.reactivex.Single

class CommentRepositoryImpl(private val commentDataSource: CommentDataSource):CommentRepository {
    override fun getCommentNumber(code: String?): Single<CommentNumber>  = commentDataSource.getCommentNumber(code)
    override fun addComment(
        user: String,
        token: String,
        code: String,
        comment: String
    ): Single<ResponseMessage> = commentDataSource.addComment(user,token,code,comment)

    override fun getComment(code: String): Single<List<Comment>> = commentDataSource.getComment(code)
}