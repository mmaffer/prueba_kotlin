package com.example.matchpet.data.repository

import com.example.matchpet.data.local.SessionManager
import com.example.matchpet.data.remote.MatchPetApiService
import com.example.matchpet.data.remote.dto.UpdateProfileRequest
import com.example.matchpet.domain.model.UserProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(
    private val apiService: MatchPetApiService,
    private val sessionManager: SessionManager
) {
    suspend fun fetchProfile(): UserProfile = withContext(Dispatchers.IO) {
        ensureAuthenticated()
        apiService.getProfile().toDomain()
    }

    suspend fun updateProfile(profile: UserProfile): UserProfile = withContext(Dispatchers.IO) {
        ensureAuthenticated()
        val request = UpdateProfileRequest(
            name = profile.name,
            phone = profile.phone,
            bio = profile.bio,
            preferences = profile.preferences
        )
        apiService.updateProfile(request).toDomain()
    }

    private fun ensureAuthenticated() {
        requireNotNull(sessionManager.getToken()) { "Token is required" }
    }
}
