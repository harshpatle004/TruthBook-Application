package com.example.truthbook.data.models

data class Post(
    val id: String,
    val userName: String,
    val userProfile: String,
    val caption: String,
    val imageUrl: Int,
    val timestamp: String,
    val likesCount: Int,
    val commentsCount: Int
)