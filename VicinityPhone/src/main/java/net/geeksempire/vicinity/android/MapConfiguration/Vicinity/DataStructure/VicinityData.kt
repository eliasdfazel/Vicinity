/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 10/23/20 6:03 AM
 * Last modified 10/23/20 5:48 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.MapConfiguration.Vicinity.DataStructure

import androidx.annotation.Keep
import com.google.android.gms.maps.model.LatLng

@Keep
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

@Keep
data class VicinityData (var centerLatitude: String, var centerLongitude: String,
                         var countryName: String, var cityName: String?,
                         var knownAddress: String?, var approximateIpAddress: String?)

@Keep
data class VicinityNotice(
    var enteredNewVicinity: Boolean,
    var userCurrentLocation: LatLng
)