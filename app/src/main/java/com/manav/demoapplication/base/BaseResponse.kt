package com.manav.demoapplication.base

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.manav.demoapplication.ApiError

open class BaseResponse<T> {
    var apiCode: Int= 0
    var apiError: ApiError? = null
    var isInternetOn = true

    @SerializedName("status")
    @Expose
    var status: Boolean = false

    @SerializedName("status_code")
    @Expose
    var status_code: Int? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("result")
    @Expose
    var result: T? = null
}