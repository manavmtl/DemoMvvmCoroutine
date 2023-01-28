package com.manav.demoapplication.ui

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.manav.demoapplication.base.BaseActivity
import com.manav.demoapplication.databinding.ActivityMainBinding
import com.manav.demoapplication.mvvm.airlines.AirlinesViewModel
import com.manav.demoapplication.mvvm.images.ImagesViewModel
import com.manav.demoapplication.network.ApiCodes
import com.manav.demoapplication.response.AllAirlines
import com.manav.demoapplication.response.AllAirlinesItem
import com.manav.demoapplication.utils.ApiError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainActivity : BaseActivity(), AdapterAirLines.AirlineCLickedListener {
    lateinit var binding: ActivityMainBinding
    lateinit var adapterAirLines: AdapterAirLines
    val airlineVM: AirlinesViewModel by lazy { ViewModelProvider(this)[AirlinesViewModel::class.java] }
    val imagesVm: ImagesViewModel by lazy { ViewModelProvider(this)[ImagesViewModel::class.java] }
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setAdapter()
        setBaseViewModel(airlineVM)
        setBaseViewModel(imagesVm)
        airlineVM.getResponseObserver().observe(this, this)
        imagesVm.getResponseObserver().observe(this, this)
//        airlineVM.getAirlinesData()
        airlineVM.getAirlineById(1)
//        airlineVM.getPassengerData()
//        imagesVm.getImages()

    }

    override fun onResponseSuccess(statusCode: Int, apiCode: Int, msg: String?) {
        Log.i(TAG, "onResponseSuccess: ApiCode $apiCode")
        Log.i(TAG, "onResponseSuccess: ${airlineVM.singleAirlinesResponse == null}")

        when (apiCode) {
            ApiCodes.ALL_AIRLINES -> {
                Log.i(TAG, "onResponseSuccess: All airline")
                airlineVM.allAirlinesResponse?.let {
                    adapterAirLines.setList(it)
                }
            }
            ApiCodes.AIRLINE_BY_ID -> {
                Log.i(TAG, "onResponseSuccess: All airline by id")
                airlineVM.singleAirlinesResponse?.let {
                    val array = AllAirlines()
                    array.add(it)
                    adapterAirLines.setList(array)
                    Log.i(TAG, "onResponseSuccess: ${it.name}")
                }
            }
            ApiCodes.IMAGES -> {
                Log.i(TAG, "onResponseSuccess: All images")
                imagesVm.imagesResponse?.let {
                    Log.i(TAG, "onResponseSuccess: ")
                }
            }
            ApiCodes.AIRLINE_PASSENGER_DATA -> {
                airlineVM.passengersData?.let {
                    Log.i(TAG, "onResponseSuccess: ${it.size}")
                }
            }
        }
    }

    override fun onException(exception: ApiError, apiCode: Int) {
        Log.i(TAG, "onException: ${exception.message}")
        when (apiCode) {
            ApiCodes.ALL_AIRLINES -> {

            }
            ApiCodes.AIRLINE_BY_ID -> {
            }
            ApiCodes.IMAGES -> {}
        }
    }

    override fun onSessionExpired(exception: ApiError, apiCode: Int) {
        Log.i(TAG, "onSessionExpired: ")
    }

    override fun noInternetConnection(apiCode: Int, msg: String?) {
        Log.i(TAG, "noInternetConnection: ")
    }


    private fun setAdapter() {
        adapterAirLines = AdapterAirLines(AllAirlines(), this)
        binding.recyclerPosts.adapter = adapterAirLines
    }

    override fun onAirlineCLicked(airlinesItem: AllAirlinesItem) {
        Log.i(TAG, "onAirlineCLicked: ${airlinesItem.name}")
    }
}