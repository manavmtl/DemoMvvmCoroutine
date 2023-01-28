package com.manav.demoapplication.mvvm.maps

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.manav.demoapplication.base.API_VIEWMODEL_DATA
import com.manav.demoapplication.base.BaseResponse
import com.manav.demoapplication.base.BaseViewModel
import com.manav.demoapplication.base.LoadingState
import com.manav.demoapplication.response.Companies
import com.manav.demoapplication.response.ImagesResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CompaniesViewModel : BaseViewModel() {
    private val mRepo: CompaniesRepo by lazy { CompaniesRepo() }
    var companies: Companies? = null

    fun getCompanies(companiesTotal: Int):LiveData<Any> {
        viewModelScope.launch {
            mRepo.getCompaniesDirect(companiesTotal)
        }
        return mRepo.companies
    }
}