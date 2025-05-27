package com.example.cheque_android.network

import android.preference.PreferenceManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    private const val port = "8080"
    private const val baseUrl = "http://10.0.2.2:$port/api/"

    fun getAccessToken(): String {
        return "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJIQWxHaGFuaW05QGdtYWlsLmNvbSIsImlhdCI6MTc0ODM2NzU2OSwiZXhwIjoxNzQ4NDUzOTY5fQ.kCYOTa97IUqCIeoRt76GBOzvMkLpPVGBnalTDulM94yKxM7A7ue4xfsvD2gSlWSElGsZEXVjDzjttfgl3ofN4g"
    }

    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(TokenInterceptor { getAccessToken() })
        .build()

    fun getInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
