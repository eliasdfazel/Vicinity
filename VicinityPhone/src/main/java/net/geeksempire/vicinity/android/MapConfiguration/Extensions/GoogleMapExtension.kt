/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 9/19/20 10:20 AM
 * Last modified 9/19/20 8:22 AM
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

    readyGoogleMap.mapType = GoogleMap.MAP_TYPE_HYBRID
    //initGoogleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(applicationContext, R.raw.map_minimal_dark_style))

}

fun MapsOfSociety.addInitialMarker() {

    userLatitudeLongitude?.let { userLatitudeLongitude ->

        userMapMarker = readyGoogleMap.addMarker(
            MarkerOptions()
                .position(userLatitudeLongitude)
                .title(firebaseUser?.displayName)
                .snippet(firebaseUser?.email)
        )

        Glide.with(this@addInitialMarker)
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
                        userMapMarker.tag = firebaseUser?.uid
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