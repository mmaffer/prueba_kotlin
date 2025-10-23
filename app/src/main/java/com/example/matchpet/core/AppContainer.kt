package com.example.matchpet.core

import android.content.Context
import com.example.matchpet.data.local.SessionManager
import com.example.matchpet.data.remote.MatchPetApiService
import com.example.matchpet.data.repository.AuthRepository
import com.example.matchpet.data.repository.UserRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

interface AppContainer {
    val sessionManager: SessionManager
    val authRepository: AuthRepository
    val userRepository: UserRepository
    val apiService: MatchPetApiService
}

class DefaultAppContainer(private val context: Context) : AppContainer {

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val requestBuilder = chain.request().newBuilder()
                sessionManager.getToken()?.let { token ->
                    requestBuilder.addHeader("Authorization", "Bearer $token")
                }
                chain.proceed(requestBuilder.build())
            }
            .addInterceptor(loggingInterceptor)
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.matchpet.dev/")
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    override val apiService: MatchPetApiService by lazy {
        retrofit.create(MatchPetApiService::class.java)
    }

    override val sessionManager: SessionManager by lazy {
        SessionManager(context)
    }

    override val authRepository: AuthRepository by lazy {
        AuthRepository(apiService, sessionManager)
    }

    override val userRepository: UserRepository by lazy {
        UserRepository(apiService, sessionManager)
    }
}
