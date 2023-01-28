package com.manav.demoapplication.mvvm.images

import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.manav.demoapplication.base.API_VIEWMODEL_DATA
import com.manav.demoapplication.base.BaseViewModel
import com.manav.demoapplication.base.LoadingState
import com.manav.demoapplication.response.ImagesResponse
import kotlinx.coroutines.*

class ImagesViewModel : BaseViewModel() {
    private val mRepo: ImagesRepo by lazy { ImagesRepo() }
    var imagesResponse: ImagesResponse? = null

    fun getImages() {
        viewModelScope.async {
            setLoadingState(LoadingState.LOADING())
            val response = mRepo.getImages()
            updateView(response) {
                when (it) {
                    is API_VIEWMODEL_DATA.API_SUCCEED -> {
                        val response_ =
                            Gson().fromJson(Gson().toJson(it.data), ImagesResponse::class.java)
                        imagesResponse = response_
                    }
                    else -> {}
                }
            }
        }
        setLoadingState(LoadingState.LOADED())
    }
}