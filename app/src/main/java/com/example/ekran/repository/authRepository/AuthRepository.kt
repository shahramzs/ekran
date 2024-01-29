package com.example.ekran.repository.authRepository

import io.reactivex.Completable

interface AuthRepository {
    fun signUp(username:String,email:String,password:String): Completable

    fun signIn(username:String,password:String):Completable

    fun loadToken()

    fun loadUsername()

    fun loadEmail()

    fun getUserName():String

    fun getEmail():String

    fun signOut()
}