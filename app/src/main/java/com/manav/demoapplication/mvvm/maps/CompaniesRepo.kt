package com.manav.demoapplication.mvvm.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.manav.demoapplication.base.BaseRepo
import com.manav.demoapplication.base.BaseResponse
import com.manav.demoapplication.network.ApiCodes
import com.manav.demoapplication.response.Companies

class CompaniesRepo:BaseRepo() {

   suspend fun getCompanies(companiesTotal:Int):BaseResponse<Any>{
        return apiRequest(ApiCodes.COMPANIES){
            imageClient.getCompanies()
        }
    }

    private val companies_=MutableLiveData<Any>()
    val companies:LiveData<Any>
    get() = companies_
    suspend fun getCompaniesDirect(companiesTotal: Int){
       val response= imageClient.getCompanies1(companiesTotal)
        if(response!=null && response.isSuccessful){
          if(response.body()!=null){
              companies_.postValue(response.body())
          }
        }
    }
}