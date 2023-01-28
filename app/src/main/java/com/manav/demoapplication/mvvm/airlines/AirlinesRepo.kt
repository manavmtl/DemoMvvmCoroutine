package com.manav.demoapplication.mvvm.airlines

import com.manav.demoapplication.base.BaseResponse
import com.manav.demoapplication.base.BaseRepo
import com.manav.demoapplication.network.ApiCodes

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

    suspend fun getPassengersData(): BaseResponse<Any> {
        return apiRequest(ApiCodes.AIRLINE_PASSENGER_DATA){
            normalClient.getPassengersData()
        }
    }
}