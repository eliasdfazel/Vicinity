/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 10/16/20 11:11 AM
 * Last modified 10/16/20 10:45 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.MapConfiguration.Extensions

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.ResultReceiver
import android.util.Log
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.functions.FirebaseFunctions
import net.geeksempire.vicinity.android.MapConfiguration.Map.MapsOfSociety
import net.geeksempire.vicinity.android.MapConfiguration.Vicinity.vicinityName
import net.geeksempire.vicinity.android.Utils.Location.KnownAddress
import net.geeksempire.vicinity.android.Utils.Location.LocationCheckpoint
import net.geeksempire.vicinity.android.Utils.Preferences.SavePreferences

fun MapsOfSociety.getLocationData() {

    val locationRequest = LocationRequest.create()
    locationRequest.interval = 1000
    locationRequest.fastestInterval = 500
    locationRequest.numUpdates = 1
    locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==  PackageManager.PERMISSION_GRANTED
        && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

        fusedLocationClient.requestLocationUpdates(locationRequest, object : LocationCallback() {

            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return

                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==  PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->

                        location?.let {

                            userLatitudeLongitude = LatLng(location.latitude, location.longitude)

                            mapView.getMapAsync(this@getLocationData)

                        }

                    }

                }

            }

        }, null)

    }

}

fun MapsOfSociety.getLocationDetails() {

    userLatitudeLongitude?.let { location ->

        val resultReceiver: ResultReceiver = object : ResultReceiver(Handler(Looper.getMainLooper())) {

            override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
                when (resultCode) {
                    KnownAddress.Constants.SUCCESS_RESULT -> {
                        val addressOutput = resultData?.getString(KnownAddress.Constants.RESULT_DATA_KEY).toString()

                        if (LocationCheckpoint.LOCATION_COUNTRY_NAME != null || LocationCheckpoint.LOCATION_CITY_NAME != null) {

                            val savePreferences = SavePreferences(applicationContext)

                            savePreferences.savePreference("VicinityInformation", vicinityName(location), LocationCheckpoint.LOCATION_INFORMATION_DETAIL)

                        } else {
                            val intent = Intent(this@getLocationDetails, KnownAddress::class.java)
                            intent.action = "LatLongData"
                            intent.putExtra(KnownAddress.Constants.RESULT_RECEIVER, this)
                            intent.putExtra(KnownAddress.Constants.LOCATION_DATA_EXTRA, location)
                            startService(intent)
                        }
                    }
                    KnownAddress.Constants.FAILURE_RESULT -> {

                        val intent = Intent(this@getLocationDetails, KnownAddress::class.java)
                        intent.action = "LatLongData"
                        intent.putExtra(KnownAddress.Constants.RESULT_RECEIVER, this)
                        intent.putExtra(KnownAddress.Constants.LOCATION_DATA_EXTRA, location)
                        startService(intent)

                    }
                }
            }
        }

        val intent = Intent(this@getLocationDetails, KnownAddress::class.java)
        intent.action = "LatLongData"
        intent.putExtra(KnownAddress.Constants.RESULT_RECEIVER, resultReceiver)
        intent.putExtra(KnownAddress.Constants.LOCATION_DATA_EXTRA, location)
        startService(intent)

    }

    FirebaseFunctions.getInstance()
        .getHttpsCallable("fetchUserPublicInternetAddress")
        .call()
        .continueWith { task ->

            val resultToContinueWith = task.result?.data as Map<String, Any>

            resultToContinueWith["ClientAddressIP"] as String

        }.addOnSuccessListener {

            LocationCheckpoint.LOCATION_KNOWN_IP = it

            Log.d(this@getLocationDetails.javaClass.simpleName, "IP Address ::: ${LocationCheckpoint.LOCATION_KNOWN_IP}")

        }.addOnFailureListener {

        }

}