package com.example.ekran.base

import androidx.annotation.StringRes

class EkranException(
    val type: Type,
    @StringRes val userFriendlyMessage: Int = 0,
    val serverMessage: String? = null
) : Throwable() {

    enum class Type {
        SIMPLE, DIALOG, AUTH
    }
}