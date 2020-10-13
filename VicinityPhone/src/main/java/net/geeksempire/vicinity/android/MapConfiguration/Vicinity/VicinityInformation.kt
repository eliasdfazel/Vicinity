/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 10/13/20 6:08 AM
 * Last modified 10/13/20 5:31 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.MapConfiguration.Vicinity

import android.content.Context
import com.google.android.gms.maps.model.LatLng
import net.geeksempire.vicinity.android.Utils.Location.LocationCheckpoint
import net.geeksempire.vicinity.android.Utils.Preferences.ReadPreferences

class VicinityInformation (context: Context) {

    private val readPreferences: ReadPreferences = ReadPreferences(context)

    fun knownLocationName(location: LatLng) : String? {

        return readPreferences.readPreference("VicinityInformation", vicinityName(location), LocationCheckpoint.LOCATION_INFORMATION_DETAIL)
    }

}