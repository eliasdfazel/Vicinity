/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 10/15/20 6:59 AM
 * Last modified 10/15/20 6:59 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.MapConfiguration.Vicinity

import android.location.Location
import com.google.android.gms.maps.model.LatLng

class VicinityCalculations {

    companion object {
        const val VicinityRadius: Double = 500.000
        const val VicinitySafeArea: Double = VicinityRadius * 2

        const val CoordinatesOffset: Double = 500.000
        const val CoordinatesOffsetRadian: Double = ((360.0 * CoordinatesOffset) / 40075000.0)

        const val MarkerClickCameraOffset: Double =  0.0001533
    }

    fun insideVicinity(userLocation: LatLng, vicinityCenter: LatLng) : Boolean {

        val pointsDistance = FloatArray(1)

        Location.distanceBetween(
            /* Current Location */
            userLocation.latitude, userLocation.longitude,
            /* Target Location */
            vicinityCenter.latitude, vicinityCenter.longitude,
            pointsDistance
        )

        return (pointsDistance[0] < VicinityRadius)
    }

    fun insideSafeDistanceVicinity(userLocation: LatLng, vicinityCenter: LatLng) : Boolean {

        val pointsDistance = FloatArray(1)

        Location.distanceBetween(
            /* Current Location */
            userLocation.latitude, userLocation.longitude,
            /* Target Location */
            vicinityCenter.latitude, vicinityCenter.longitude,
            pointsDistance
        )

        return (pointsDistance[0] < (VicinityRadius + (VicinityRadius/2)))
    }

    fun joinedVicinity(userLocation: LatLng, vicinityCenter: LatLng) : Boolean {

        return (insideVicinity(userLocation, vicinityCenter) || insideSafeDistanceVicinity(userLocation, vicinityCenter))
    }

}