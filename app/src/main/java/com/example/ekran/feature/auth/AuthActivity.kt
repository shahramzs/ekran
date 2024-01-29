package com.example.ekran.feature.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ekran.R
import com.example.ekran.feature.auth.fragments.SignInFragment

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainerAuth, SignInFragment())
        }.commit()
    }
}