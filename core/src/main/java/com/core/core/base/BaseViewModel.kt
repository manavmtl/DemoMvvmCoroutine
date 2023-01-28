package com.core.core.base

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.core.core.network.ApiCodes
import com.core.core.utils.ApiError

open class BaseViewModel : ViewModel() {

    private val _loadingState = MutableLiveData<LoadingState>()
    val loadingState: LiveData<LoadingState>
        get() = _loadingState

    private val _response = MutableLiveData<ApiResponseData>()
    private val response: LiveData<ApiResponseData>
        get() = _response

    private fun updateResponseObserver(response: ApiResponseData) {
        _response.value = response
    }

    fun getResponseObserver() = response


    /**
     *
     *
     * @param baseData response coming from backend
     * @param callBack lambda callback update view model
     */
    fun updateView(baseData: BaseResponse<Any>?, callBack: (API_VIEWMODEL_DATA) -> Unit) {
        if (baseData != null) {
            if (baseData.isInternetOn) {
                Log.i("BaseViewModel", "updateView response code: ${baseData.status_code}")
                if (baseData.status_code != null && (baseData.status_code == 200 ||
                            baseData.status_code == 202 || baseData.status_code == 201)
                ) {
                    callBack(API_VIEWMODEL_DATA.API_SUCCEED(baseData.result, baseData.apiCode))
                    updateResponseObserver(
                        ApiResponseData.API_SUCCEED(
                            baseData.status_code!!,
                            baseData.apiCode,
                            baseData.message
                        )
                    )
                    return
                } else if (baseData.status_code != null && baseData.status_code == 401) {
                    callBack(API_VIEWMODEL_DATA.API_EXPIRED_SESSION(baseData.apiCode))
                    updateResponseObserver(
                        ApiResponseData.API_EXPIRED_SESSION(
                            ApiError(),
                            baseData.apiCode
                        )
                    )

                } else {
                    Log.i("BaseViewModel", "updateView: exception")
                    callBack(API_VIEWMODEL_DATA.API_EXCEPTION(baseData.apiCode))
                    updateResponseObserver(
                        ApiResponseData.API_EXCEPTION(
                            baseData.apiError!!,
                            baseData.apiCode
                        )
                    )
                    return
                }
            } else {
                callBack(API_VIEWMODEL_DATA.NO_INTERNET(baseData.apiCode))
                updateResponseObserver(
                    ApiResponseData.NO_INTERNET(
                        baseData.apiCode,
                        baseData.apiError!!.message
                    )
                )
                return
            }
        }
        val error = ApiError()
        error.message = "Something went wrong"
        callBack(API_VIEWMODEL_DATA.API_EXCEPTION(ApiCodes.EMPTY_RESPONSE))
        updateResponseObserver(ApiResponseData.API_EXCEPTION(error, ApiCodes.EMPTY_RESPONSE))
        return
    }

    fun setLoadingState(state: LoadingState) {
        _loadingState.value = state
    }
}