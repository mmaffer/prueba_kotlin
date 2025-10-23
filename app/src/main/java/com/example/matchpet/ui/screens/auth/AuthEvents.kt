package com.example.matchpet.ui.screens.auth

sealed interface RegisterEvent {
    data class NameChanged(val value: String) : RegisterEvent
    data class EmailChanged(val value: String) : RegisterEvent
    data class PasswordChanged(val value: String) : RegisterEvent
    data class ConfirmPasswordChanged(val value: String) : RegisterEvent
    data object Submit : RegisterEvent
    data object SuccessConsumed : RegisterEvent
}

sealed interface LoginEvent {
    data class EmailChanged(val value: String) : LoginEvent
    data class PasswordChanged(val value: String) : LoginEvent
    data object Submit : LoginEvent
    data object SuccessConsumed : LoginEvent
}

sealed interface EditProfileEvent {
    data class NameChanged(val value: String) : EditProfileEvent
    data class PhoneChanged(val value: String) : EditProfileEvent
    data class BioChanged(val value: String) : EditProfileEvent
    data class PreferencesChanged(val value: String) : EditProfileEvent
    data object Submit : EditProfileEvent
    data object MessageConsumed : EditProfileEvent
}
