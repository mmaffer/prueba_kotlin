package com.example.matchpet.domain.model

data class UserProfile(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val bio: String,
    val preferences: String
)
