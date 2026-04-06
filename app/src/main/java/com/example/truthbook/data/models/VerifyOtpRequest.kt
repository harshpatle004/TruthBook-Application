package com.example.truthbook.data.models

data class VerifyOtpRequest(
    val email: String,
    val otp: String
)