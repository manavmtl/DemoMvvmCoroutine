package com.manav.demoapplication

import android.app.Application
import com.manav.demoapplication.utils.CoreContextWrapper

class AppClass : Application() {

    override fun onCreate() {
        super.onCreate()

        CoreContextWrapper.setContext(this)
    }

    override fun onTerminate() {
        super.onTerminate()

    }
}