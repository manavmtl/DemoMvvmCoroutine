package com.core.core

import android.app.Application
import com.core.core.utils.CoreContextWrapper

class AppClass : Application() {

    override fun onCreate() {
        super.onCreate()

        CoreContextWrapper.setContext(this)
    }

    override fun onTerminate() {
        super.onTerminate()

    }
}