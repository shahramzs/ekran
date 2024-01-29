package com.example.ekran.base

import com.example.ekran.R
import org.json.JSONObject
import retrofit2.HttpException
import timber.log.Timber

class EkranExceptionMapper {

    companion object{
        fun map(throwable: Throwable):EkranException{
            if(throwable is HttpException){
                try {
                    val errorJsonObject = JSONObject(throwable.response()?.errorBody()!!.string())
                    if(errorJsonObject.has("message")){
                        val errorMessage = errorJsonObject.getString("message")
                        return EkranException(if(throwable.code() == 401) EkranException.Type.AUTH else EkranException.Type.SIMPLE, serverMessage = errorMessage)
                    }else if(errorJsonObject.has("non_field_errors")){
                        val errorNonField = errorJsonObject.getString("non_field_errors")
                        return EkranException(if(throwable.code() == 401) EkranException.Type.AUTH else EkranException.Type.SIMPLE, serverMessage = errorNonField)
                    }else if(errorJsonObject.has("username")){
                        val errorUsername = errorJsonObject.getString("username")
                        return EkranException(if(throwable.code() == 401) EkranException.Type.AUTH else EkranException.Type.SIMPLE, serverMessage = errorUsername)
                    }else if(errorJsonObject.has("password")){
                        val errorPassword = errorJsonObject.getString("password")
                        return EkranException(if(throwable.code() == 401) EkranException.Type.AUTH else EkranException.Type.SIMPLE, serverMessage = errorPassword)
                    }else if(errorJsonObject.has("email")){
                        val errorEmail = errorJsonObject.getString("email")
                        return EkranException(if(throwable.code() == 401) EkranException.Type.AUTH else EkranException.Type.SIMPLE, serverMessage = errorEmail)
                    }else if(errorJsonObject.has("detail")){
                        val errorDetail = errorJsonObject.getString("detail")
                        return EkranException(if(throwable.code() == 401) EkranException.Type.AUTH else EkranException.Type.SIMPLE, serverMessage = errorDetail)
                    }

                }catch (e:Exception){
                    Timber.tag("error").e(e)
                }
            }
            return EkranException(EkranException.Type.SIMPLE, R.string.unknown_error)

        }
    }
}