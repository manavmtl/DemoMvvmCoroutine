package com.manav.demoapplication.mvvm

import com.manav.demoapplication.ApiCodes
import com.manav.demoapplication.base.BaseRepo
import com.manav.demoapplication.base.BaseResponse

class Repository : BaseRepo() {

    suspend fun getPostsRepo(): BaseResponse<Any> {
        return apiRequest(ApiCodes.POSTS) {
            unauthenticatedApiClient.getPosts()
        }
    }
}