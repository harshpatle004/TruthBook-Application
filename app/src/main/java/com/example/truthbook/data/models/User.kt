package com.example.truthbook.data.models

data class User(
    val fullName: String,
    val username: String,
    val bio: String?,
    val profileImage: String,
    val coverImage: String,
    val isPrivate: Boolean,
    val followers: List<String>,
    val following: List<String>,
    val posts: List<String>
)