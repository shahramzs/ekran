package com.example.ekran.dataSource.authDataSource

import com.example.ekran.model.AuthResponse
import com.example.ekran.model.ResponseMessage
import com.example.ekran.services.http.ApiService
import com.google.gson.JsonObject
import io.reactivex.Single

class AuthRemoteDataSource(private val apiService: ApiService):AuthDataSource {
    override fun signUp(
        username: String,
        email: String,
        password: String
    ): Single<ResponseMessage> = apiService.signUp(JsonObject().apply {
        addProperty("username",username)
        addProperty("email",email)
        addProperty("password",password)
    })

    override fun signIn(username: String, password: String): Single<AuthResponse> =  apiService.signIn(JsonObject().apply {
        addProperty("username",username)
        addProperty("password",password)
    })

    override fun loadToken() {
        TODO("Not yet implemented")
    }

    override fun loadUsername() {
        TODO("Not yet implemented")
    }

    override fun loadEmail() {
        TODO("Not yet implemented")
    }

    override fun saveToken(token: String) {
        TODO("Not yet implemented")
    }

    override fun saveUserName(username: String) {
        TODO("Not yet implemented")
    }

    override fun saveEmail(email: String) {
        TODO("Not yet implemented")
    }

    override fun getUserName(): String {
        TODO("Not yet implemented")
    }

    override fun getEmail(): String {
        TODO("Not yet implemented")
    }

    override fun signOut() {
        TODO("Not yet implemented")
    }
}