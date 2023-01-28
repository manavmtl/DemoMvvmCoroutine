package com.core.core.mvvm.airlines

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.core.core.base.*
import com.core.core.response.AllAirlines
import com.core.core.response.AllAirlinesItem
import kotlinx.coroutines.async

class AirlinesViewModel : BaseViewModel() {
    private val mRepo by lazy { AirlinesRepo() }
    var allAirlinesResponse: AllAirlines? = null
    var singleAirlinesResponse: AllAirlinesItem? = null

    fun getAirlinesData() {
        viewModelScope.async {
            setLoadingState(LoadingState.LOADING())
            val resp = mRepo.getAllAirlines()
            updateView(
                resp
            ) {
                when (it) {
                    is API_VIEWMODEL_DATA.API_SUCCEED -> {
                        Log.i("MainActivity", "getAirlinesData: ")
                        val response =
                            Gson().fromJson(Gson().toJson(it.data), AllAirlines::class.java)
                        allAirlinesResponse = response
                    }
                    else -> {}
                }
            }
            setLoadingState(LoadingState.LOADED())
        }
    }

    fun getAirlineById(id: Int) {
        viewModelScope.async {
            setLoadingState(LoadingState.LOADING())
            val response = mRepo.getAirlineById(id)
            updateView(response) {
                when (it) {
                    is API_VIEWMODEL_DATA.API_EXCEPTION -> {

                    }
                    is API_VIEWMODEL_DATA.API_EXPIRED_SESSION -> {}
                    is API_VIEWMODEL_DATA.API_SUCCEED -> {
                        Log.i("AirlinesViewModel", "getAirlineById: ${it.apiCode}")
                        val response_ =
                            Gson().fromJson(Gson().toJson(it.data), AllAirlinesItem::class.java)
                        singleAirlinesResponse = response_
                    }
                    else -> {}
                }
            }
            setLoadingState(LoadingState.LOADED())
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}