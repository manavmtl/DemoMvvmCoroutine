package com.manav.demoapplication.base

import com.manav.demoapplication.base.GenericApiRequest
import com.manav.demoapplication.network.RetrofitObject

open class BaseRepo : GenericApiRequest<Any>() {

    val normalClient = RetrofitObject.apiService
    val imageClient = RetrofitObject.apiServiceImage

}