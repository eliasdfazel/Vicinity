/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 9/1/20 4:37 AM
 * Last modified 9/1/20 4:37 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.MapConfiguration.Extensions

import com.google.android.gms.maps.GoogleMap
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

    readyGoogleMap.clear()



}