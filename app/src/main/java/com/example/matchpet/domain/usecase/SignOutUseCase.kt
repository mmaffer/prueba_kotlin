package com.example.matchpet.domain.usecase

import com.example.matchpet.data.repository.AuthRepository

class SignOutUseCase(private val authRepository: AuthRepository) {
    operator fun invoke() = authRepository.signOut()
}
