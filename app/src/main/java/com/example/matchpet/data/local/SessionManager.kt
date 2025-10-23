package com.example.matchpet.data.local

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class SessionManager(context: Context) {

    private val preferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val tokenFlow = MutableStateFlow(preferences.getString(KEY_TOKEN, null))

    fun saveToken(token: String) {
        preferences.edit().putString(KEY_TOKEN, token).apply()
        tokenFlow.update { token }
    }

    fun clearToken() {
        preferences.edit().remove(KEY_TOKEN).apply()
        tokenFlow.update { null }
    }

    fun getToken(): String? = preferences.getString(KEY_TOKEN, null)

    fun observeToken(): Flow<String?> = tokenFlow

    companion object {
        private const val PREFS_NAME = "matchpet_prefs"
        private const val KEY_TOKEN = "auth_token"
    }
}
