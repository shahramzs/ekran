package com.example.ekran.dataSource.authDataSource

import com.example.ekran.model.AuthResponse
import com.example.ekran.model.ResponseMessage
import io.reactivex.Single

interface AuthDataSource {

    fun signUp(username:String,email:String,password:String): Single<ResponseMessage>

    fun signIn(username:String,password:String): Single<AuthResponse>

    fun loadToken()

    fun loadUsername()

    fun loadEmail()

    fun saveToken(token:String)

    fun saveUserName(username:String)

    fun saveEmail(email:String)

    fun getUserName():String

    fun getEmail():String

    fun signOut()
}