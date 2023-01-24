package com.manav.demoapplication

import android.app.Application

class AppClass : Application() {

    override fun onCreate() {
        super.onCreate()

        CoreContextWrapper.setContext(this)
    }
}