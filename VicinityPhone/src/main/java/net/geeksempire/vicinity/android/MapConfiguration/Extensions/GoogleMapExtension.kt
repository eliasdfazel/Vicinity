/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 9/1/20 10:10 AM
 * Last modified 9/1/20 10:10 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.MapConfiguration.Extensions

import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.MarkerOptions
import net.geeksempire.vicinity.android.MapConfiguration.Map.MapsOfSociety

fun MapsOfSociety.setupGoogleMap() {
    googleMapIsReady = true

    readyGoogleMap.setOnMapClickListener(this@setupGoogleMap)
    readyGoogleMap.setOnMapLongClickListener(this@setupGoogleMap)

    readyGoogleMap.setOnMarkerClickListener(this@setupGoogleMap)

    readyGoogleMap.setOnCameraMoveListener(this@setupGoogleMap)
    readyGoogleMap.setOnCameraIdleListener(this@setupGoogleMap)

    readyGoogleMap.uiSettings.setAllGesturesEnabled(true)
    readyGoogleMap.uiSettings.isZoomControlsEnabled = false
    readyGoogleMap.uiSettings.isMapToolbarEnabled = false
    readyGoogleMap.uiSettings.isCompassEnabled = false

    readyGoogleMap.isTrafficEnabled = true

    /*If Style Selected as Detailed*/
    readyGoogleMap.mapType = GoogleMap.MAP_TYPE_HYBRID
    //No Style

    /*If Style Selected as Minimal Dark | Change Color of Status Bar & Vicinity Circle Color to LIGHT*/
    //googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
    //initGoogleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(applicationContext, R.raw.map_minimal_dark_style))

}

fun MapsOfSociety.drawVicinity() {

    userLatitudeLongitude?.let { userLatitudeLongitude ->

        readyGoogleMap.clear()

//        val circleOptions = CircleOptions()
//            .center(communityLatLong)
//            .radius(if (listDistance[0] > vicinityRadius) { vicinitySafeArea } else { vicinityRadius })
//            .strokeColor(getColor(R.color.light))
//            .fillColor(getColor(R.color.light_transparent_vicinity))
//            .strokeWidth(3.70f)
//            .clickable(true)
//        readyGoogleMap.addCircle(circleOptions)

        userMapMarker = readyGoogleMap.addMarker(
            MarkerOptions()
                .position(userLatitudeLongitude)
                .title(firebaseUser?.displayName)
                .snippet(firebaseUser?.email)
        )

        Glide.with(this@drawVicinity)
            .asDrawable()
            .load(firebaseUser?.photoUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .listener(object : RequestListener<Drawable> {

                override fun onLoadFailed(glideException: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {

                    return false
                }

                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {

                    runOnUiThread {

                        readyGoogleMap.clear()

                        userMapMarker = readyGoogleMap.addMarker(
                            MarkerOptions()
                                .position(userLatitudeLongitude)
                                .title(firebaseUser?.displayName)
                                .snippet(firebaseUser?.email)
                        )
                        userMapMarker.isDraggable = false

                        mapsLiveData.currentLocationData.postValue(userLatitudeLongitude)

                        locationCoordinatesUpdater.startProcess()

                    }

                    return false
                }

            })
            .submit()

    }

}