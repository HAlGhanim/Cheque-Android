package com.example.cheque_android.network

import android.accounts.Account
import com.example.cheque_android.data.dto.*
import com.example.cheque_android.data.request.*
import com.example.cheque_android.data.response.*
import retrofit2.Response
import retrofit2.http.*

interface ChequeApiService {

    // 🔐 Authentication
    @POST("/api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<TokenResponse>

    @POST("/api/auth/login")
    suspend fun login(@Body user: User): Response<TokenResponse>

    // 👤 KYC
    @POST("/api/kyc")
    suspend fun createKyc(@Body request: KYCRequest): Response<Unit>

    @GET("/api/users/me")
    suspend fun getCurrentUser(): Response<User>

    // 💳 Accounts
    @POST("/api/accounts/create")
    suspend fun createAccount(
        @Body accountRequest: AccountRequest
    ): Response<Unit>

    @GET("/api/accounts/my")
    suspend fun getMyAccount(): Response<List<Account>>

    // 💸 Transactions
    @GET("/api/transactions/my")
    suspend fun getMyTransactions(string: String): Response<List<TransactionResponse>>

    @POST("/api/redeem/use/{code}")
    suspend fun redeemCode(
        @Path("code") code: String,
    ): Response<Map<String, String>>

    // 🛠️ Admin Endpoints
    @GET("/api/admin/dashboard")
    suspend fun getDashboardStats(): Response<DashboardStats>

    @GET("/api/admin/users")
    suspend fun getUsers(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("role") role: String? = null
    ): Response<List<User>>

    @GET("/api/admin/users/{userId}")
    suspend fun getUserById(@Path("userId") userId: Long): Response<User>

    @DELETE("/api/admin/users/{userId}")
    suspend fun deleteUser(@Path("userId") userId: Long): Response<Unit>

    @POST("/api/admin/users/{userId}/reset-password")
    suspend fun resetPassword(@Path("userId") userId: Long): Response<String>

    @POST("/api/admin/users/{userId}/suspend")
    suspend fun suspendUser(@Path("userId") userId: Long): Response<Unit>

    @GET("/api/admin/accounts/getAll")
    suspend fun getAllAccounts(): Response<List<Account>>

    @GET("/api/admin/accounts/id/{id}")
    suspend fun getAccountById(@Path("id") id: Long): Response<Account>

    @GET("/api/admin/accounts/{accountNumber}")
    suspend fun getAccountByNumber(@Path("accountNumber") accountNumber: String): Response<Account>

    @GET("/api/admin/transactions/getAll")
    suspend fun getAllTransactions(): Response<List<TransactionResponse>>

    @GET("/api/admin/transactions/id/{id}")
    suspend fun getTransactionById(@Path("id") id: Long): Response<TransactionResponse>

    @GET("/api/admin/transactions/account/{accountNumber}")
    suspend fun getTransactionsByAccountNumber(@Path("accountNumber") accountNumber: String): Response<List<TransactionResponse>>

    @GET("/api/admin/transfers/getAll")
    suspend fun getAllTransfers(): Response<List<TransferResponse>>

    @GET("/api/admin/transfers/id/{id}")
    suspend fun getTransferById(@Path("id") id: Long): Response<TransferResponse>

    @GET("/api/admin/transfers/user/{userId}")
    suspend fun getTransfersByUserId(@Path("userId") userId: Long): Response<List<TransferResponse>>

    @GET("/api/admin/transfers/transaction/{transactionId}")
    suspend fun getTransfersByTransactionId(@Path("transactionId") transactionId: Long): Response<List<TransferResponse>>

    @GET("/api/admin/transfers/account/{accountNumber}")
    suspend fun getTransfersByAccountNumber(@Path("accountNumber") accountNumber: String): Response<List<TransferResponse>>

    @GET("/api/admin/payment-links/getAll")
    suspend fun getAllPaymentLinks(): Response<List<PaymentLinkResponse>>

    @GET("/api/admin/payment-links/id/{id}")
    suspend fun getPaymentLinkByIdDirect(@Path("id") id: Long): Response<PaymentLinkResponse>

    @GET("/api/admin/payment-links/{id}")
    suspend fun getPaymentLinkById(@Path("id") id: Long): Response<PaymentLinkResponse>

    @DELETE("/api/admin/payment-links/{id}")
    suspend fun deletePaymentLink(@Path("id") id: Long): Response<Unit>

    @GET("/api/admin/kyc/getAll")
    suspend fun getAllKYC(): Response<List<KYC>>

    @GET("/api/admin/kyc/{id}")
    suspend fun getKYCById(@Path("id") id: Long): Response<KYC>

    @GET("/api/admin/redeem/active/count")
    suspend fun getActiveCodeCount(): Response<Map<String, Int>>

    @POST("/api/admin/redeem/generate")
    suspend fun generateRedeemCode(@Body request: RedeemRequest): Response<Map<String, Any>>
}