/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 10/19/20 10:39 AM
 * Last modified 10/19/20 9:56 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.MapConfiguration.Vicinity.DataStructure

import com.google.android.gms.maps.model.LatLng

object VicinityDataStructure {
    const val lastLatitude = "lastLatitude"
    const val lastLongitude = "lastLongitude"
    const val userJointDate = "userJointDate"
    const val vicinityCountry = "vicinityCountry"
    const val vicinityLatitude = "vicinityLatitude"
    const val vicinityLongitude = "vicinityLongitude"
    const val vicinityName = "vicinityName"
    const val vicinityKnownName = "vicinityKnownName"
}

data class VicinityData (var centerLatitude: String, var centerLongitude: String,
                         var countryName: String, var cityName: String?,
                         var knownAddress: String?, var approximateIpAddress: String?)

data class VicinityNotice(
    var enteredNewVicinity: Boolean,
    var userCurrentLocation: LatLng
)