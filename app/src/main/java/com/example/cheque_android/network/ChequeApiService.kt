package com.example.cheque_android.network

import com.example.cheque_android.data.Account
import com.example.cheque_android.data.AccountRequest
import com.example.cheque_android.data.KYCRequest
import com.example.cheque_android.data.RegisterRequests
import com.example.cheque_android.data.User
import com.example.cheque_android.data.response.TokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ChequeApiService {

    @POST("/api/auth/register")
    suspend fun register(@Body request: RegisterRequests): Response<TokenResponse>


    @POST("/api/kyc")
    suspend fun createKyc(@Body request: KYCRequest): Response<Unit>

    @POST("/api/accounts/create")
    suspend fun createAccount(
        @Header("Authorization") token: String,
        @Body accountRequest: AccountRequest): Response<Unit>

    @POST("/auth/login")
    suspend fun login(@Body user: User): Response<TokenResponse>


    @GET("accounts/my")
    suspend fun getMyAccount(): Response<List<Account>>


//    @PUT(Constants.depositEndpoint)
//    suspend fun deposit(@Header("Authorization") token: String?,
//                        @Body amountChange: AmountChange
//    ): Response<Unit>

// ما رح نستخدم هذا بس موجود عشان نعرف شكل الendpoint الي يحتاج token
}