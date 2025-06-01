package com.example.cheque_android.network

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor(private val tokenProvider: () -> String?) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val token = tokenProvider()
        val newRequest = if (!token.isNullOrBlank()) {
            Log.d("Interceptor", "Original request: ${originalRequest.url}")
            Log.d("Interceptor", "Token from TokenManager: $token")

            originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            Log.d("Interceptor", "Original request: ${originalRequest.url}")
            Log.d("Interceptor", "Token from TokenManager: $token")

            originalRequest
        }
        val response = chain.proceed(newRequest)
        if (response.code == 401) {
            Log.w("Interceptor", "Token expired or unauthorized")
        }
        return response
    }

}