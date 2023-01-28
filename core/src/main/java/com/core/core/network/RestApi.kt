package com.core.core.network

import com.core.core.base.BaseResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface RestApi {

    @GET(ApiEndpoints.ALL_AIRLINES)
    suspend fun getAllAirlines(): Response<BaseResponse<Any>>

    @GET("${ApiEndpoints.ALL_AIRLINES}/{id}")
    suspend fun getAirlineById(@Path("id") id: Int): Response<BaseResponse<Any>>

}