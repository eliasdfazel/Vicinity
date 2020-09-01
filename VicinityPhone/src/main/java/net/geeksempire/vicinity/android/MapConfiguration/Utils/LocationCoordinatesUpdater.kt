/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 9/1/20 9:44 AM
 * Last modified 9/1/20 8:49 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.chat.vicinity.Util.MapsUtil

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import com.google.android.gms.maps.model.LatLng
import net.geeksempire.vicinity.android.MapConfiguration.LocationDataHolder.MapsLiveData


class LocationCoordinatesUpdater (private val context: Context, private val mapsLiveData: MapsLiveData) : LocationListener {

    private var lastLocation: Location? = null

    private var latitude: Double = 0.toDouble()
    private var longitude: Double = 0.toDouble()

    private var locationManager: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    companion object {
        private const val MIN_DISTANCE_CHANGE_FOR_UPDATES: Long = (3).toLong()
        private const val MIN_TIME_BETWEEN_UPDATES = (1000 * 1 * 60) * (13).toLong()
    }

    val locationLatitudeLongitude: LatLng
        get() = LatLng(getLatitude(), getLongitude())

    fun startProcess() {

        if ((context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            && (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {

            getLocation()

        }

    }

    override fun onLocationChanged(location: Location) {

        latitude = location.latitude
        longitude = location.longitude

        mapsLiveData.currentLocationData.postValue(LatLng(latitude, longitude))

    }

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {

    }

    override fun onProviderEnabled(provider: String) {

    }

    override fun onProviderDisabled(provider: String) {

    }

    private fun getLocation(): Location? {
        try {

            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {



            } else {

                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                    if (lastLocation == null) {

                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BETWEEN_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(), this@LocationCoordinatesUpdater)

                        lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

                        if (lastLocation != null) {

                            latitude = lastLocation!!.latitude
                            longitude = lastLocation!!.longitude

                            mapsLiveData.currentLocationData.postValue(LatLng(latitude, longitude))

                        }

                    }

                }

                if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BETWEEN_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(), this@LocationCoordinatesUpdater)

                    lastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

                    if (lastLocation != null) {

                        latitude = lastLocation!!.latitude
                        longitude = lastLocation!!.longitude

                        mapsLiveData.currentLocationData.postValue(LatLng(latitude, longitude))

                    }

                }

            }

        } catch (e: SecurityException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return lastLocation
    }

    fun stopUsingLocationUpdater() {

        locationManager.removeUpdates(this@LocationCoordinatesUpdater)
    }

    fun getLatitude(): Double {

        lastLocation?.let {
            latitude = it.latitude
        }

        return latitude
    }

    fun getLongitude(): Double {

        lastLocation?.let {
            longitude = it.longitude
        }

        return longitude
    }

    fun canGetLocationData(): Boolean {

        return ((context.getSystemService(Context.LOCATION_SERVICE) as LocationManager).isProviderEnabled(LocationManager.GPS_PROVIDER)
                && (context.getSystemService(Context.LOCATION_SERVICE) as LocationManager).isProviderEnabled(LocationManager.NETWORK_PROVIDER))
    }

}