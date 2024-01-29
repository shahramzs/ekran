package com.example.ekran.base

import timber.log.Timber

object TokenContainer {

    var token: String? = null
        private set

    var username: String? = null
        private set

    var email:String ?= null
        private set

    fun updateToken(token:String?){
        Timber.tag("token").i(token)
        this.token = token
    }

    fun updateUsername(username:String?){
        this.username = username
    }

    fun updateEmail(email:String?){
        this.email = email
    }
}