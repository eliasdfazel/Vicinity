/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 9/3/20 8:48 AM
 * Last modified 9/3/20 7:07 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.MapConfiguration.Vicinity

import com.google.android.gms.maps.model.LatLng

fun vicinityName(locationLatitudeLongitude: LatLng) : String {

    return locationLatitudeLongitude.latitude.toString().replace(".", "") +
            "|" +
            locationLatitudeLongitude.longitude.toString().replace(".", "")
}