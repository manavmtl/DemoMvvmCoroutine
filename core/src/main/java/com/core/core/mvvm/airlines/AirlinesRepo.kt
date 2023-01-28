package com.core.core.mvvm.airlines

import com.core.core.base.BaseResponse
import com.core.core.base.BaseRepo
import com.core.core.network.ApiCodes

class AirlinesRepo : BaseRepo() {


    suspend fun getAllAirlines() : BaseResponse<Any> {
        return apiRequest(ApiCodes.ALL_AIRLINES) {
            normalClient.getAllAirlines()
        }
    }

    suspend fun getAirlineById(id:Int): BaseResponse<Any> {
        return apiRequest(ApiCodes.AIRLINE_BY_ID){
            normalClient.getAirlineById(id)
        }
    }
}