package com.tut.mynewsredoplayground

import android.app.Application
import timber.log.Timber

class NewsApp:Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}