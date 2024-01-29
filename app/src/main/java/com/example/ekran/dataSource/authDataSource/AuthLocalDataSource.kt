package com.example.ekran.dataSource.authDataSource

import android.content.SharedPreferences
import com.example.ekran.base.TokenContainer
import com.example.ekran.model.AuthResponse
import com.example.ekran.model.ResponseMessage
import io.reactivex.Single

class AuthLocalDataSource(private val sharedPreferences: SharedPreferences):AuthDataSource {

    override fun signUp(username: String, email: String, password: String): Single<ResponseMessage> {
        TODO("Not yet implemented")
    }

    override fun signIn(username: String, password: String): Single<AuthResponse> {
        TODO("Not yet implemented")
    }

    override fun loadToken() {
        TokenContainer.updateToken(sharedPreferences.getString("token", null))
    }

    override fun loadUsername() {
        TokenContainer.updateUsername(sharedPreferences.getString("username", null))
    }

    override fun loadEmail() {
        TokenContainer.updateEmail(sharedPreferences.getString("email", null))
    }

    override fun saveToken(token: String) {
        sharedPreferences.edit().apply {
            putString("token", token)
        }.apply()
    }

    override fun saveUserName(username: String) {
        sharedPreferences.edit().apply {
            putString("username", username)
        }.apply()
    }

    override fun saveEmail(email: String) {
        sharedPreferences.edit().apply {
            putString("email", email)
        }.apply()
    }

    override fun getUserName(): String = sharedPreferences.getString("username", "") ?: ""

    override fun getEmail(): String = sharedPreferences.getString("email", "") ?: ""

    override fun signOut() = sharedPreferences.edit().apply {
        clear()
    }.apply()
}