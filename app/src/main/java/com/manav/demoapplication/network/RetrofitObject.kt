package com.manav.demoapplication.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitObject {

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder().baseUrl(NetworkConstants.BASE_URL)
            .client(getLoggingClient())
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    private val retrofitImage: Retrofit by lazy {
        Retrofit.Builder().baseUrl(NetworkConstants.BASE_URL_IMAGES)
            .client(getLoggingClient())
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    val apiService: RestApi by lazy {
        retrofit.create(RestApi::class.java)
    }

    val apiServiceImage: RestApi by lazy {
        retrofitImage.create(RestApi::class.java)
    }

    private fun getLoggingClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
    }
}