package com.codemobiles.myapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.codemobiles.mymap.requestOpenGPS
import com.codemobiles.mymap.showToastLong
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener

// top level
const val UPDATE_INTERVAL: Long = 5000
const val FASTEST_INTERVAL: Long = 1000

class MapsActivity : AppCompatActivity() {

    private lateinit var mMap: GoogleMap

    private var mCurrentLocation: Location? = null
    private var mLocationProvider: FusedLocationProviderClient? = null
    private var mCallback: LocationCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        mLocationProvider = LocationServices.getFusedLocationProviderClient(this)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync { googleMap ->
            mMap = googleMap
            setupMap() // mute be call after mMap = googleMap
            checkRuntimePermission()
            dummyLocation()
        }
    }

    override fun onStart() {
        super.onStart()

        showToastLong("Hello map")
    }

    override fun onResume() {
        super.onResume()

        requestOpenGPS()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }


    private fun dummyLocation() {
        val myHomeLocation = LatLng(13.696993, 100.515182)
        addMarker("my home", "125212/ 1", myHomeLocation)

        mCurrentLocation?.let { currentLocation ->
            val myCurrentLocation = LatLng(currentLocation.latitude, currentLocation.longitude)
            addMarker("my home", "125212/ 1", myCurrentLocation)
        }
    }

    private fun addMarker(title: String, subTitle: String, latLng: LatLng) {
        val marker = MarkerOptions()
        marker.position(latLng)
        marker.title(title)
        marker.snippet(subTitle)
        marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.logo))

        mMap.addMarker(marker)
    }

    private fun setupMap() {
        val initLocation = LatLng(13.9821187, 100.5582046)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initLocation, 14f))

        mMap.isTrafficEnabled = true

        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isZoomControlsEnabled = true

        mMap.mapType = GoogleMap.MAP_TYPE_HYBRID

        mMap.setOnMapClickListener { latLng ->
            showToastLong("${latLng.latitude} , ${latLng.longitude}")
        }

        mMap.setOnMarkerClickListener { marker ->
            showToastLong("${marker.position.latitude} , ${marker.position.longitude}")

            marker.showInfoWindow()

            true
        }

        mMap.setOnInfoWindowClickListener { marker ->
            val startString = "${mCurrentLocation!!.latitude},${mCurrentLocation!!.longitude}"
            val destString = "${marker.position.latitude},${marker.position.longitude}"

            val url = "http://maps.google.com/maps?saddr=$startString&daddr=$destString"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
    }

    private fun checkRuntimePermission() {
        Dexter.withContext(this)
            .withPermission(
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            .withListener(object : PermissionListener {
                @SuppressLint("MissingPermission")
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    mMap.isMyLocationEnabled = true
                    getLastLocation()
                    //tracking()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    finish()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).check()
    }

    @SuppressLint("MissingPermission")
    private fun tracking() {
        val request = LocationRequest()
        request.interval = UPDATE_INTERVAL
        request.fastestInterval = FASTEST_INTERVAL
        request.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        mCallback = object : LocationCallback() {
            override fun onLocationResult(resut: LocationResult?) {
                super.onLocationResult(resut)
                val currentLocation = resut!!.lastLocation
                if (currentLocation != null) {
                    mCurrentLocation = currentLocation
                    animateCamera(LatLng(currentLocation.latitude, currentLocation.longitude), 15)
                }
            }
        }

        mLocationProvider!!.requestLocationUpdates(request, mCallback, Looper.myLooper())
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        mLocationProvider?.lastLocation
            ?.addOnSuccessListener { location: Location? ->
                mCurrentLocation = location
                if (location != null) {
                    animateCamera(LatLng(location.latitude, location.longitude), 15)
                }
            }
    }

    private fun animateCamera(latLng: LatLng, zoom: Int) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom.toFloat()))
    }

}
