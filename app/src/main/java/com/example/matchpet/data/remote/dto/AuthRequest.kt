package com.example.matchpet.data.remote.dto

data class AuthRequest(
    val name: String? = null,
    val email: String,
    val password: String
)
