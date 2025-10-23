package com.example.matchpet.domain.usecase

import com.example.matchpet.data.local.SessionManager
import kotlinx.coroutines.flow.Flow

class ObserveSessionUseCase(private val sessionManager: SessionManager) {
    operator fun invoke(): Flow<String?> = sessionManager.observeToken()
}
