/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 9/1/20 4:35 AM
 * Last modified 9/1/20 4:35 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.MapConfiguration.LocationDataHolder

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async

class MapsLiveData : ViewModel() {

    val currentLocationData: MutableLiveData<LatLng> by lazy {
        MutableLiveData<LatLng>()
    }

    fun getLastKnownLocation() = CoroutineScope(SupervisorJob() + Dispatchers.IO).async {


    }

}