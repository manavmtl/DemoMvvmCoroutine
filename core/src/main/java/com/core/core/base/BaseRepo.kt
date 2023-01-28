package com.core.core.base

import com.core.core.base.GenericApiRequest
import com.core.core.network.RetrofitObject

open class BaseRepo : GenericApiRequest<Any>() {

    val normalClient = RetrofitObject.apiService

}