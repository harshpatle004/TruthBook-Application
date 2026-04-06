package com.example.truthbook.data.remote

import com.example.truthbook.data.models.ApiResponse
import com.example.truthbook.data.models.EmailRequest
import com.example.truthbook.data.models.LoginRequest
import com.example.truthbook.data.models.SendOtpRequest
import com.example.truthbook.data.models.SetPasswordRequest
import com.example.truthbook.data.models.SetUsernameRequest
import com.example.truthbook.data.models.VerifyOtpRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("api/auth/send-otp")
    suspend fun sendOtp(@Body request: SendOtpRequest): Response<ApiResponse>   // ✅ fullName + email

    @POST("api/auth/login")
    suspend fun login(@Body  request: LoginRequest) : Response<ApiResponse>
    @POST("api/auth/verify-otp")
    suspend fun verifyOtp(@Body request: VerifyOtpRequest): Response<ApiResponse>

    @POST("api/auth/set-password")                                               // ✅ NEW endpoint
    suspend fun setPassword(@Body request:SetPasswordRequest): Response<ApiResponse>

    @POST("api/auth/set-username")
    suspend fun setUsername(@Body request: SetUsernameRequest): Response<ApiResponse>

    @POST("api/auth/resend-otp")                                                 // ✅ NEW endpoint
    suspend fun resendOtp(@Body request:EmailRequest): Response<ApiResponse>
}