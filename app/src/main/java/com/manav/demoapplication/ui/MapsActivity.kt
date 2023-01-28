package com.manav.demoapplication.ui

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.gson.Gson
import com.manav.demoapplication.R
import com.manav.demoapplication.databinding.ActivityMapsBinding
import com.manav.demoapplication.mvvm.maps.CompaniesViewModel
import com.manav.demoapplication.response.Companies


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    lateinit var binding: ActivityMapsBinding
    lateinit var map: SupportMapFragment
    lateinit var googleMap: GoogleMap
    private var bounds: LatLngBounds? = null
    private var builder: LatLngBounds.Builder? = null
    private var listMarkers: ArrayList<Marker> = ArrayList()
    val companiesVM: CompaniesViewModel by lazy { ViewModelProvider(this)[CompaniesViewModel::class.java] }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        map = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        map.getMapAsync(this)

        companiesVM.getCompanies(8).observe(
            this
        ) { value ->
            val response = Gson().fromJson(Gson().toJson(value), Companies::class.java)
            Log.i("MapsActivity", "onCreate: ${response.data.size}")
            Log.i("MapsActivity", "onCreate: ${response.data[0].city}")
            val it = response.data
            it.forEach {
                val latLng = LatLng(it.latitude, it.longitude)
                val name = it.city
                val id = it.id
                addMarker(latLng, name, id, it.country)
            }
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),100)
            }
        }
        googleMap.isMyLocationEnabled=false
    }

    private fun addMarker(latLng: LatLng, name: String, id: Int, country: String) {
        val markerOptions = MarkerOptions().position(latLng)
            .title(name).snippet(country)
            .anchor(0.5f, 0.5f)

        val marker = googleMap.addMarker(
            markerOptions
        )

        listMarkers.add(marker!!)
    }
}