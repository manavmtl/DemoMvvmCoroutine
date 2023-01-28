package com.manav.demoapplication.network

sealed class NetworkState<T>(val data: T? = null, val message: String? = null) {

    class Loading<T>() : NetworkState<T>()
    class Success<T>(data: T?, message: String?) : NetworkState<T>(data, message)
    class Failed<T>(data: T? = null, message: String? = null) : NetworkState<T>(data, message)
}
