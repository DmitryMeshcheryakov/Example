package com.android.example.core.modules.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import com.android.example.core.app.BaseApplication
import java.util.*

/**
 *
 * Connects to available provider and onResume tracking location
 * don't forget disconnect() when view paused
 *
 */

class LocationHelper(private var context: Context) : LocationListener {

    companion object {
        const val TAG = "LOCATION"

        const val MIN_UPDATE_TIME = 5000L //ms
        const val MIN_UPDATE_DISTANCE = 0f //m
    }

    var locationErrorListener: ((t: Throwable) -> Unit)? = null
    var locationChangeListener: ((lat: Double, lon: Double) -> Unit)? = null

    var currentLatitude: Double? = null
    var currentLongitude: Double? = null

    private var locationManager: LocationManager? = null

    init {
        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    @SuppressLint("MissingPermission")
    fun connect() {
        try {
            when {
                locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER) == true -> {
                    locationManager?.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        MIN_UPDATE_TIME,
                        MIN_UPDATE_DISTANCE, this
                    )
                    BaseApplication.logger.d(TAG, "connected to GPS_PROVIDER")
                }
                locationManager?.isProviderEnabled(LocationManager.NETWORK_PROVIDER) == true -> {
                    locationManager?.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        MIN_UPDATE_TIME,
                        MIN_UPDATE_DISTANCE, this
                    )
                    BaseApplication.logger.d(TAG, "connected to NETWORK_PROVIDER")
                }
            }
            saveNewLocation(locationManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER))
        } catch (e: SecurityException) {
            locationErrorListener?.invoke(e)
        } catch (e: Exception) {
            locationErrorListener?.invoke(e)
        }
    }

    @SuppressLint("MissingPermission")
    fun disconnect() {
        BaseApplication.logger.d(TAG, "disconnect")
        locationManager?.removeUpdates(this)
    }

    fun getAddress(
        latitude: Double,
        longitude: Double,
        locale: Locale = Locale.getDefault()
    ): String {
        val geo = geocoder().getFromLocation(latitude, longitude, 1)
        return if (geo?.isEmpty() == false) geo[0].getAddressLine(0).orEmpty() else ""
    }

    fun geocoder(locale: Locale = Locale.getDefault()) = Geocoder(context, locale)

    private fun saveNewLocation(location: Location?) {
        if (location != null) {
            currentLatitude = location.latitude
            currentLongitude = location.longitude

            locationChangeListener?.invoke(location.latitude, location.longitude)
            BaseApplication.logger.d(TAG, "location changed $currentLatitude $currentLongitude")
        }
    }

    override fun onLocationChanged(p0: Location?) {
        saveNewLocation(p0)
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {

    }

    override fun onProviderEnabled(p0: String?) {
        connect()
    }

    override fun onProviderDisabled(p0: String?) {
        connect()
    }
}