package com.example.cheque_android.network

import android.content.Context
import com.example.cheque_android.utils.TokenManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    private const val port = "8080"
    private const val baseUrl = "http://10.0.2.2:$port/api/"

    fun getInstance(context: Context): Retrofit {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(TokenInterceptor {
                TokenManager.getToken(context)
            })
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
