package com.core.core.base

import com.core.core.utils.ApiError
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

open class BaseResponse<T> {
    var apiCode: Int = 0
    var apiError: ApiError? = null
    var isInternetOn = true

    @SerializedName("status")
    @Expose
    var status: Boolean = false

    @SerializedName("code")
    @Expose
    var status_code: Int? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("total")
    @Expose
    var total: String? = null

    @SerializedName("result")
    @Expose
    var result: T? = null
}