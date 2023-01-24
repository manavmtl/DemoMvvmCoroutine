package com.manav.demoapplication.retrofit

import com.manav.demoapplication.ApiConstants
import com.manav.demoapplication.Posts
import com.manav.demoapplication.base.BaseResponse
import retrofit2.Response
import retrofit2.http.GET

interface RestApi {
    @GET(ApiConstants.posts)
    suspend fun getPosts(): Response<BaseResponse<Any>>
}