package com.example.matchpet.data.remote.dto

import com.example.matchpet.domain.model.UserProfile

data class ProfileResponse(
    val id: String,
    val name: String,
    val email: String,
    val phone: String?,
    val bio: String?,
    val preferences: String?
) {
    fun toDomain(): UserProfile = UserProfile(
        id = id,
        name = name,
        email = email,
        phone = phone.orEmpty(),
        bio = bio.orEmpty(),
        preferences = preferences.orEmpty()
    )
}
