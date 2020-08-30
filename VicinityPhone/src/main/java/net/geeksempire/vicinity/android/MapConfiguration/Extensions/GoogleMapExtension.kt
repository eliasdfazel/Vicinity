/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 8/30/20 6:54 AM
 * Last modified 8/30/20 6:49 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.MapConfiguration.Extensions

import com.google.android.gms.maps.GoogleMap
import net.geeksempire.vicinity.android.MapConfiguration.Map.MapsOfSociety

fun MapsOfSociety.MapExtension() {
    googleMapIsReady = true

    readyGoogleMap.setOnMapClickListener(this@MapExtension)
    readyGoogleMap.setOnMapLongClickListener(this@MapExtension)

    readyGoogleMap.setOnMarkerClickListener(this@MapExtension)

    readyGoogleMap.setOnCameraMoveListener(this@MapExtension)
    readyGoogleMap.setOnCameraIdleListener(this@MapExtension)

    readyGoogleMap.uiSettings.setAllGesturesEnabled(true)
    readyGoogleMap.uiSettings.isZoomControlsEnabled = false
    readyGoogleMap.uiSettings.isMapToolbarEnabled = false
    readyGoogleMap.uiSettings.isCompassEnabled = false

    readyGoogleMap.isTrafficEnabled = true

    /*If Style Selected as Detailed*/
    readyGoogleMap.mapType = GoogleMap.MAP_TYPE_HYBRID
    //No Style

    /*If Style Selected as Minimal Light | Change Color of Status Bar & Vicinity Circle Color to DARK*/
    //googleMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
    //No Style

    /*If Style Selected as Minimal Dark | Change Color of Status Bar & Vicinity Circle Color to LIGHT*/
    //googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
    //initGoogleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(applicationContext, R.raw.map_minimal_dark_style))
}