package com.example.matchpet.domain.usecase

import com.example.matchpet.data.local.SessionManager

class GetCurrentTokenUseCase(private val sessionManager: SessionManager) {
    operator fun invoke(): String? = sessionManager.getToken()
}
