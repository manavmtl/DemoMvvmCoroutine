package com.manav.demoapplication.network

import com.manav.demoapplication.base.BaseResponse
import com.manav.demoapplication.response.AllAirlines
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RestApi {

    @GET(ApiEndpoints.ALL_AIRLINES)
    suspend fun getAllAirlines(): Response<BaseResponse<Any>>

    @GET("airlines/{id}")
    suspend fun getAirlineById(@Path("id") id: Int): Response<BaseResponse<Any>>

    @GET("passenger?page=0&size=10")
    suspend fun getPassengersData(): Response<BaseResponse<Any>>

    @GET("images?_width=380")
    suspend fun getImages(): Response<BaseResponse<Any>>

    @GET("addresses?_quantity=1")
    suspend fun getCompanies(): Response<BaseResponse<Any>>

    @GET("addresses?")
    suspend fun getCompanies1(@Query("_quantity") total:Int): Response<Any>
}