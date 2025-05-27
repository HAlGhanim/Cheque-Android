package com.example.cheque_android.network

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor(private val tokenProvider: () -> String?) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val token = tokenProvider()
        val newRequest = if (!token.isNullOrBlank()) {
            originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            originalRequest
        }
        val response = chain.proceed(newRequest)
        if (response.code == 401) {
            Log.w("Interceptor", "Token expired or unauthorized")
        }
        return response
    }

}