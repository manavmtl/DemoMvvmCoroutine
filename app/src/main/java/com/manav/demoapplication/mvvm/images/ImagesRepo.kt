package com.manav.demoapplication.mvvm.images

import com.manav.demoapplication.base.BaseResponse
import com.manav.demoapplication.base.BaseRepo
import com.manav.demoapplication.network.ApiCodes

class ImagesRepo: BaseRepo() {

    suspend fun getImages(): BaseResponse<Any> {
        return apiRequest(ApiCodes.IMAGES){
            imageClient.getImages()
        }
    }
}