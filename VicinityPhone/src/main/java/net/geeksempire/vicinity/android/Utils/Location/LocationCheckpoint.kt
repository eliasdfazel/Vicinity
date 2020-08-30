/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 8/30/20 9:15 AM
 * Last modified 8/30/20 9:10 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.Utils.Location

import android.content.Context
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import net.geeksempire.vicinity.android.MapConfiguration.Map.MapsOfSociety

class LocationCheckpoint {

    fun turnOnGps(appCompatActivity: AppCompatActivity) {

        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val locationSettingsRequest = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        locationSettingsRequest.setAlwaysShow(true)

        val pendingResult = LocationServices.getSettingsClient(appCompatActivity).checkLocationSettings(locationSettingsRequest.build())
        pendingResult.addOnCompleteListener {
            try {
                if (it.exception != null) {
                    when ((it.exception as ApiException).statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {

                            val resolvable = it.exception as ResolvableApiException
                            resolvable.startResolutionForResult(appCompatActivity, MapsOfSociety.GpsEnableRequestCode)
                        }
                    }
                }
            } catch (exception: ApiException) {
                exception.printStackTrace()
            }
        }

    }

    fun gpsAvailable(context: Context): Boolean {

        val locationManager: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        return (isGpsEnabled && isNetworkEnabled)
    }

}