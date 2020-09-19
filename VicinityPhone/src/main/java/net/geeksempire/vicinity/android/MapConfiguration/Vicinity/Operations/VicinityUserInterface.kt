/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 9/19/20 10:20 AM
 * Last modified 9/19/20 9:58 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.MapConfiguration.Vicinity.Operations

import android.animation.ValueAnimator
import android.content.Context
import android.location.Location
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import net.geeksempire.vicinity.android.MapConfiguration.Vicinity.VicinityCalculations
import net.geeksempire.vicinity.android.R

class VicinityUserInterface (private val context: Context) {

    fun drawVicinity(googleMap: GoogleMap, locationVicinity: LatLng, userLatitudeLongitude: LatLng) {

        val pointsDistance = FloatArray(1)

        Location.distanceBetween(
            /* Current Location */
            userLatitudeLongitude.latitude, userLatitudeLongitude.longitude,
            /* Target Location */
            locationVicinity.latitude, locationVicinity.longitude,
            pointsDistance
        )

        val circleOptions = CircleOptions()
            .center(locationVicinity)
            .radius(if (pointsDistance[0] > VicinityCalculations.VicinityRadius) { VicinityCalculations.VicinitySafeArea } else { VicinityCalculations.VicinityRadius })
            .strokeColor(context.getColor(R.color.light))
            .fillColor(context.getColor(R.color.light_transparent_vicinity))
            .strokeWidth(3.70f)
            .clickable(true)

        googleMap.addCircle(circleOptions)

        val valueAnimatorCameraMovement = ValueAnimator.ofFloat(13.77f, 15.70f)
        valueAnimatorCameraMovement.duration = 777
        valueAnimatorCameraMovement.addUpdateListener { animator ->
            val animatorValue = animator.animatedValue as Float

            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatitudeLongitude, animatorValue))

        }
        valueAnimatorCameraMovement.start()

    }

}