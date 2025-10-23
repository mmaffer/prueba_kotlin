package com.example.matchpet.ui.screens.auth

import com.example.matchpet.domain.model.UserProfile

data class AuthState(
    val register: RegisterUiState = RegisterUiState(),
    val login: LoginUiState = LoginUiState(),
    val profile: ProfileUiState = ProfileUiState(),
    val editProfile: EditProfileUiState = EditProfileUiState()
)

data class RegisterUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)

data class ProfileUiState(
    val isLoading: Boolean = false,
    val profile: UserProfile? = null,
    val error: String? = null
)

data class EditProfileUiState(
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val bio: String = "",
    val preferences: String = "",
    val isLoading: Boolean = false,
    val message: String? = null,
    val error: String? = null
)
