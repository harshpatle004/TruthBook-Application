package com.example.truthbook.data.models

import org.intellij.lang.annotations.Identifier

data class LoginRequest(
    val identifier: String,
    val password: String
)