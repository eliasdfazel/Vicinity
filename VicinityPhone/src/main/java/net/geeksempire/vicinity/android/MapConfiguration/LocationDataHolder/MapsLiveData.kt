/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 10/19/20 10:39 AM
 * Last modified 10/19/20 10:39 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.MapConfiguration.LocationDataHolder

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import net.geeksempire.vicinity.android.MapConfiguration.Vicinity.DataStructure.VicinityNotice
import net.geeksempire.vicinity.android.MapConfiguration.Vicinity.VicinityCalculations

class MapsLiveData : ViewModel() {

    val currentLocationData: MutableLiveData<LatLng> by lazy {
        MutableLiveData<LatLng>()
    }

    val vicinityNotice: MutableLiveData<VicinityNotice> by lazy {
        MutableLiveData<VicinityNotice>()
    }

    fun calculateVicinityCoordinates(userCurrentLocation: LatLng, vicinityCenterLocation: LatLng) = CoroutineScope(SupervisorJob() + Dispatchers.IO).async {

        val vicinityCalculations: VicinityCalculations = VicinityCalculations()

        if (vicinityCalculations.insideVicinity(userCurrentLocation, vicinityCenterLocation)) {
            Log.d(this@async.javaClass.simpleName, "Still In Same Vicinity")



        } else if (vicinityCalculations.insideSafeDistanceVicinity(userCurrentLocation, vicinityCenterLocation)) {
            Log.d(this@async.javaClass.simpleName, "Still In Same Vicinity | Safe Distance")



        } else {
            Log.d(this@async.javaClass.simpleName, "Entered New Vicinity")

            vicinityNotice.postValue(VicinityNotice(
                enteredNewVicinity = true,
                userCurrentLocation = userCurrentLocation
            ))

        }

    }

}