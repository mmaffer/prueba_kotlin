package com.example.matchpet.data.repository

import com.example.matchpet.data.local.SessionManager
import com.example.matchpet.data.remote.MatchPetApiService
import com.example.matchpet.data.remote.dto.AuthRequest
import com.example.matchpet.data.remote.dto.AuthResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository(
    private val apiService: MatchPetApiService,
    private val sessionManager: SessionManager
) {
    suspend fun register(name: String, email: String, password: String): AuthResponse =
        withContext(Dispatchers.IO) {
            val response = apiService.register(AuthRequest(name = name, email = email, password = password))
            sessionManager.saveToken(response.token)
            response
        }

    suspend fun login(email: String, password: String): AuthResponse = withContext(Dispatchers.IO) {
        val response = apiService.login(AuthRequest(email = email, password = password))
        sessionManager.saveToken(response.token)
        response
    }

    suspend fun loginWithGoogle(idToken: String): AuthResponse = withContext(Dispatchers.IO) {
        val response = apiService.loginWithGoogle("Bearer $idToken")
        sessionManager.saveToken(response.token)
        response
    }

    fun signOut() {
        sessionManager.clearToken()
    }
}
