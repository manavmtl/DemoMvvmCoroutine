package com.manav.demoapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.manav.demoapplication.base.BaseResponse
import com.manav.demoapplication.mvvm.MyViewModel

class MainActivity : AppCompatActivity(), Observer<BaseResponse<Any>> {
    val mViewModel by lazy { ViewModelProvider(this@MainActivity)[MyViewModel::class.java] }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        mViewModel.getPosts().observe(this, this)
    }

    override fun onChanged(it: BaseResponse<Any>) {
        Log.i("posts", it!!.apiCode.toString())
        Log.i("posts", it!!.message.toString())
        Log.i("posts", it!!.toString())
    }
}