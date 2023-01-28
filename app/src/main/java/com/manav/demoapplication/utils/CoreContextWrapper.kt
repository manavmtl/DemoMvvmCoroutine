package com.manav.demoapplication.utils

import android.content.Context

object CoreContextWrapper {
    private lateinit var mApplication: Context

    fun getContext(): Context {
        return mApplication
    }

    fun setContext(application: Context) {
        mApplication = application
    }
}