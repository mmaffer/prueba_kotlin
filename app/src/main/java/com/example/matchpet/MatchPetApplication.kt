package com.example.matchpet

import android.app.Application
import com.example.matchpet.core.AppContainer
import com.example.matchpet.core.DefaultAppContainer

class MatchPetApplication : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(applicationContext)
    }
}
