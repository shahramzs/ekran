package com.example.ekran.repository.authRepository

import com.example.ekran.base.TokenContainer
import com.example.ekran.dataSource.authDataSource.AuthDataSource
import com.example.ekran.model.AuthResponse
import io.reactivex.Completable

class AuthRepositoryImpl(private val authRemoteDataSource: AuthDataSource, private val authLocalDataSource: AuthDataSource):AuthRepository {
    override fun signUp(
        username: String,
        email: String,
        password: String
    ): Completable{
        return authRemoteDataSource.signUp(username,email,password).flatMap {
            authRemoteDataSource.signIn(username,password).doOnSuccess {
                onSuccessfulLogin(it)
            }
        }.ignoreElement()
    }

    override fun signIn(username: String, password: String): Completable {
       return authRemoteDataSource.signIn(username,password).doOnSuccess {
           onSuccessfulLogin(it)
       }.ignoreElement()
    }
    override fun loadToken() = authLocalDataSource.loadToken()

    override fun loadUsername()  = authLocalDataSource.loadUsername()

    override fun loadEmail() = authLocalDataSource.loadEmail()

    override fun getUserName(): String = authLocalDataSource.getUserName()

    override fun getEmail(): String = authLocalDataSource.getEmail()

    override fun signOut() = authLocalDataSource.signOut()

    private fun onSuccessfulLogin(authResponse: AuthResponse){
        TokenContainer.updateToken(authResponse.token)
        TokenContainer.updateUsername(authResponse.username)
        TokenContainer.updateEmail(authResponse.email)
        authLocalDataSource.saveToken(authResponse.token)
        authLocalDataSource.saveEmail(authResponse.email)
        authLocalDataSource.saveUserName(authResponse.username)
    }
}