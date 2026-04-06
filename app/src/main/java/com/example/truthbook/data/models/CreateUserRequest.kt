package com.example.truthbook.data.models

data class CreateUserRequest(
    val fullName: String,
    val email: String,
    val password: String
)