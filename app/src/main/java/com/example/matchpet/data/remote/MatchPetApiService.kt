package com.example.matchpet.data.remote

import com.example.matchpet.data.remote.dto.AuthRequest
import com.example.matchpet.data.remote.dto.AuthResponse
import com.example.matchpet.data.remote.dto.ProfileResponse
import com.example.matchpet.data.remote.dto.UpdateProfileRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface MatchPetApiService {

    @POST("auth/register")
    suspend fun register(@Body request: AuthRequest): AuthResponse

    @POST("auth/login")
    suspend fun login(@Body request: AuthRequest): AuthResponse

    @POST("auth/google")
    suspend fun loginWithGoogle(@Header("Authorization") idToken: String): AuthResponse

    @GET("user/profile")
    suspend fun getProfile(): ProfileResponse

    @PUT("user/profile")
    suspend fun updateProfile(@Body request: UpdateProfileRequest): ProfileResponse
}
