package com.manav.demoapplication.base

import com.manav.demoapplication.retrofit.GenericApiRequest
import com.manav.demoapplication.retrofit.RetrofitClass

/**
 * Base class for all repos using in App
 *
 */
open class BaseRepo : GenericApiRequest<Any>() {
    protected val unauthenticatedApiClient = RetrofitClass.getRetroInstance().getService()

}
