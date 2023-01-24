package com.manav.demoapplication.retrofit

import com.manav.demoapplication.ApiConstants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClass {
   private val retrofit: Retrofit by lazy {
        Retrofit.Builder().baseUrl(ApiConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    val retrofitService: RestApi by lazy {
        retrofit.create(RestApi::class.java)
    }


    fun getService(): RestApi {
        return retrofit.create(RestApi::class.java)
    }

    companion object {

        var instance: RetrofitClass? = null

        fun getRetroInstance(): RetrofitClass {
            if (instance == null) {
                instance =
                    RetrofitClass()
            }
            return instance as RetrofitClass
        }

    }
}