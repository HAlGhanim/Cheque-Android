package com.example.cheque_android.network

import android.accounts.Account
import com.example.cheque_android.data.AccountResponse
import com.example.cheque_android.data.DashboardStats
import com.example.cheque_android.data.KYC
import com.example.cheque_android.data.User
import com.example.cheque_android.data.request.RedeemRequest
import com.example.cheque_android.data.response.AuthResponse
import com.example.cheque_android.data.response.PaymentLinkResponse
import com.example.cheque_android.data.response.TransactionResponse
import com.example.cheque_android.data.response.TransferResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ChequeApiService {

    @POST("auth/register")
    suspend fun signup(@Body user: User): Response<AuthResponse>

    @POST("auth/login")
    suspend fun login(@Body user: User): Response<AuthResponse>

    @GET("accounts/my")
    suspend fun getMyAccount(): Response<List<Account>>


    // Admin Endpoints
    @GET("admin/dashboard")
    suspend fun getDashboardStats(): Response<DashboardStats>

    @GET("admin/users")
    suspend fun getUsers(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("role") role: String? = null
    ): Response<List<User>>

    @GET("admin/users/{userId}")
    suspend fun getUserById(@Path("userId") userId: Long): Response<User>

    @DELETE("admin/users/{userId}")
    suspend fun deleteUser(@Path("userId") userId: Long): Response<Unit>

    @POST("admin/users/{userId}/reset-password")
    suspend fun resetPassword(@Path("userId") userId: Long): Response<String>

    @POST("admin/users/{userId}/suspend")
    suspend fun suspendUser(@Path("userId") userId: Long): Response<Unit>

    @GET("admin/accounts/getAll")
    suspend fun getAllAccounts(): Response<List<AccountResponse>>

    @GET("admin/accounts/id/{id}")
    suspend fun getAccountById(@Path("id") id: Long): Response<AccountResponse>

    @GET("admin/accounts/{accountNumber}")
    suspend fun getAccountByNumber(@Path("accountNumber") accountNumber: String): Response<AccountResponse>

    @GET("admin/transactions/getAll")
    suspend fun getAllTransactions(): Response<List<TransactionResponse>>

    @GET("admin/transactions/id/{id}")
    suspend fun getTransactionById(@Path("id") id: Long): Response<TransactionResponse>

    @GET("admin/transactions/account/{accountNumber}")
    suspend fun getTransactionsByAccountNumber(@Path("accountNumber") accountNumber: String): Response<List<TransactionResponse>>

    @GET("admin/transfers/getAll")
    suspend fun getAllTransfers(): Response<List<TransferResponse>>

    @GET("admin/transfers/id/{id}")
    suspend fun getTransferById(@Path("id") id: Long): Response<TransferResponse>

    @GET("admin/transfers/user/{userId}")
    suspend fun getTransfersByUserId(@Path("userId") userId: Long): Response<List<TransferResponse>>

    @GET("admin/transfers/transaction/{transactionId}")
    suspend fun getTransfersByTransactionId(@Path("transactionId") transactionId: Long): Response<List<TransferResponse>>

    @GET("admin/transfers/account/{accountNumber}")
    suspend fun getTransfersByAccountNumber(@Path("accountNumber") accountNumber: String): Response<List<TransferResponse>>

    @GET("admin/payment-links/getAll")
    suspend fun getAllPaymentLinks(): Response<List<PaymentLinkResponse>>

    @GET("admin/payment-links/id/{id}")
    suspend fun getPaymentLinkByIdDirect(@Path("id") id: Long): Response<PaymentLinkResponse>

    @GET("admin/payment-links/{id}")
    suspend fun getPaymentLinkById(@Path("id") id: Long): Response<PaymentLinkResponse>

    @DELETE("admin/payment-links/{id}")
    suspend fun deletePaymentLink(@Path("id") id: Long): Response<Unit>

    @GET("admin/kyc/getAll")
    suspend fun getAllKYC(): Response<List<KYC>>

    @GET("admin/kyc/{id}")
    suspend fun getKYCById(@Path("id") id: Long): Response<KYC>

    @GET("admin/redeem/active/count")
    suspend fun getActiveCodeCount(): Response<Map<String, Int>>

    @POST("admin/redeem/generate")
    suspend fun generateRedeemCode(@Body request: RedeemRequest): Response<Map<String, Any>>

    @GET("users/me")
    suspend fun getCurrentUser(): Response<User>


//    @PUT(Constants.depositEndpoint)
//    suspend fun deposit(@Header("Authorization") token: String?,
//                        @Body amountChange: AmountChange
//    ): Response<Unit>

// ما رح نستخدم هذا بس موجود عشان نعرف شكل الendpoint الي يحتاج token
}