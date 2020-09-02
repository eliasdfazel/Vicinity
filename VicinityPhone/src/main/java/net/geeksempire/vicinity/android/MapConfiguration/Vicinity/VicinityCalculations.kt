/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 9/2/20 5:26 AM
 * Last modified 9/2/20 5:26 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.MapConfiguration.Vicinity

import android.location.Location
import com.google.android.gms.maps.model.LatLng

class VicinityCalculations {

    companion object {
        const val vicinityRadius: Double = 500.000
        const val vicinitySafeArea: Double = vicinityRadius * 2
        const val coordinatesOffset: Double = 500.000
        const val coordinatesOffsetRadian: Double = ((360.0 * coordinatesOffset) / 40075000.0)
    }

    fun insideVicinity(userLocation: LatLng, vicinityCenter: LatLng) : Boolean{

        val pointsDistance = FloatArray(1)

        Location.distanceBetween(
            /* Current Location */
            userLocation.latitude, userLocation.longitude,
            /* Target Location */
            vicinityCenter.latitude, vicinityCenter.longitude,
            pointsDistance
        )

        return (pointsDistance[0] < vicinityRadius)
    }

}