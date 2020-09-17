/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 9/17/20 9:59 AM
 * Last modified 9/17/20 9:49 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.Utils.Location

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.text.Html
import android.text.Spannable
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.maps.model.LatLng
import net.geeksempire.vicinity.android.MapConfiguration.Map.MapsOfSociety
import net.geeksempire.vicinity.android.Utils.Preferences.ReadPreferences
import java.util.*

class LocationCheckpoint {

    companion object {
        var LOCATION_INFORMATION_DETAIL: String? = null
        var LOCATION_INFORMATION_DETAIL_HTML: Spannable? = null

        var LOCATION_COUNTRY_NAME: String? = null
        var LOCATION_STATE_PROVINCE: String? = null
        var LOCATION_CITY_NAME: String? = null
        var LOCATION_KNOWN_NAME: String? = null

        var LOCATION_KNOWN_IP: String? = null
    }

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

    fun getCountryCode(context: Context) : String? {

        val readPreferences = ReadPreferences(context)

        return readPreferences.readPreference("UserInformation", "CountryCode", "+1")
    }

    fun loadLocationInfo(context: Context, latLng: LatLng): ArrayList<Address>? {

        var locationAddresses: ArrayList<Address>? = null

        LocationCheckpoint.LOCATION_INFORMATION_DETAIL = "Unknown (${latLng.latitude}.${latLng.longitude}"

        try {
            val geocoder: Geocoder = Geocoder(context, Locale.getDefault())
            locationAddresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1) as ArrayList<Address>

            if (locationAddresses.size > 0) {

                LocationCheckpoint.LOCATION_COUNTRY_NAME = locationAddresses[0].countryName
                LocationCheckpoint.LOCATION_STATE_PROVINCE = locationAddresses[0].adminArea
                LocationCheckpoint.LOCATION_CITY_NAME = locationAddresses[0].locality

                val knownName = locationAddresses[0].featureName
                if (knownName != null) {

                    LocationCheckpoint.LOCATION_KNOWN_NAME = knownName
                    LocationCheckpoint.LOCATION_INFORMATION_DETAIL = "${LocationCheckpoint.LOCATION_COUNTRY_NAME}, ${LocationCheckpoint.LOCATION_STATE_PROVINCE}, ${LocationCheckpoint.LOCATION_CITY_NAME}, $knownName"
                    LocationCheckpoint.LOCATION_INFORMATION_DETAIL_HTML = Html.fromHtml("<big><b>${LocationCheckpoint.LOCATION_COUNTRY_NAME}</b></big> | <big>${LocationCheckpoint.LOCATION_CITY_NAME}</big><br/>" + "${knownName}") as Spannable

                } else {

                    LocationCheckpoint.LOCATION_INFORMATION_DETAIL = "${LocationCheckpoint.LOCATION_COUNTRY_NAME}, ${LocationCheckpoint.LOCATION_STATE_PROVINCE}, ${LocationCheckpoint.LOCATION_CITY_NAME}"

                }

            } else {

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return locationAddresses
    }

}