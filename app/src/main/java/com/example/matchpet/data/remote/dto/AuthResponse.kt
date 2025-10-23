package com.example.matchpet.data.remote.dto

data class AuthResponse(
    val token: String,
    val user: ProfileResponse
)
