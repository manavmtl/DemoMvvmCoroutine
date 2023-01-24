package com.manav.demoapplication.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.manav.demoapplication.Posts
import com.manav.demoapplication.base.BaseResponse
import com.manav.demoapplication.base.CoroutinesBase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyViewModel : ViewModel() {
    private val posts = MutableLiveData<BaseResponse<Any>>()
    private val getPosts: LiveData<BaseResponse<Any>>
        get() = posts

    private val mRepo by lazy { Repository() }
    public var postsResponse: Posts? = null

    fun getPosts(): LiveData<BaseResponse<Any>> {
        CoroutinesBase.main {
            val result = mRepo.getPostsRepo()
            posts.value = result
        }
        return getPosts
    }

    fun getPostsWithout() {
        CoroutineScope(Dispatchers.Main).launch {
            val result = mRepo.getPostsRepo()
            posts.value = result

            val response =
                Gson().fromJson(Gson().toJson(result), Posts::class.java)
            postsResponse = response
        }
    }
}