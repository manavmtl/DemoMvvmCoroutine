package com.core.core.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.core.core.utils.LoadingDialog

abstract class BaseActivity : AppCompatActivity(), LiveDataObserver<ApiResponseData> {
    private var baseViewModel: BaseViewModel? = null
    private var progressDialog: LoadingDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    fun setBaseViewModel(viewModel: BaseViewModel) {
        this.baseViewModel = viewModel
        baseViewModel?.loadingState?.observe(this, Observer { loadingState ->
            if (loadingState != null) {
                when (loadingState) {
                    is LoadingState.LOADING -> showProgressDialog(
                        loadingState.type,
                        loadingState.msg
                    )
                    is LoadingState.LOADED -> hideProgressDialog(
                        loadingState.type,
                        loadingState.msg
                    )
                }
            }
        })
    }

    open fun showProgressDialog(type: Int, msg: String) {
        if (!isDestroyed) {
            when (type) {
                LoaderType.NORMAL -> {
                    progressDialog = LoadingDialog(this)
                    progressDialog?.show()
                }
            }

        }
    }

    open fun hideProgressDialog(type: Int, msg: String) {
        when (type) {
            LoaderType.NORMAL -> {
                progressDialog?.let {
                    if (it.isShowing)
                        it.dismiss()
                }
            }
        }
    }

}