package com.example.ekran.feature.auth

import com.example.ekran.base.EkranViewModel
import com.example.ekran.repository.authRepository.AuthRepository
import io.reactivex.Completable

class AuthViewModel(private val authRepository: AuthRepository): EkranViewModel() {

    fun signIn(username:String,password:String):Completable{
        progressBarLiveData.postValue(true)
        return authRepository.signIn(username,password).doFinally{
            progressBarLiveData.postValue(false)
        }
    }

    fun signUp(username:String,email:String,password:String):Completable{
        progressBarLiveData.postValue(true)
        return authRepository.signUp(username,email,password).doFinally {
            progressBarLiveData.postValue(false)
        }
    }
}