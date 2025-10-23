package com.example.matchpet.data.remote.dto

data class UpdateProfileRequest(
    val name: String,
    val phone: String,
    val bio: String,
    val preferences: String
)
