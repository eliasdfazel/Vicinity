/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 9/15/20 10:09 AM
 * Last modified 9/15/20 9:14 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.AccountManager.Utils

import android.content.Context
import com.google.android.gms.maps.model.LatLng
import net.geeksempire.vicinity.android.Utils.Preferences.ReadPreferences
import net.geeksempire.vicinity.android.Utils.Preferences.SavePreferences

class UserInformationIO(private val context: Context) {

    fun saveUserInformation(userEmailAddress: String) {

        val savePreferences = SavePreferences(context)

        savePreferences.savePreference("UserInformation", "Email", userEmailAddress)

    }

    fun getUserAccountName() : String? {

        return ReadPreferences(context).readPreference("UserInformation", "Email", null)
    }

    fun userSignedIn() : Boolean {

        return (ReadPreferences(context).readPreference("UserInformation", "Email", "Unknown") != "Unknown")
    }

    fun saveUserLocation(userLocation: LatLng) {

        val savePreferences = SavePreferences(context)

        savePreferences.savePreference("UserInformation", "Latitude", userLocation.latitude.toString())
        savePreferences.savePreference("UserInformation", "Longitude", userLocation.longitude.toString())

    }

    fun readUserLocation() : LatLng? {

        var userLocation: LatLng? = null

        val readPreferences = ReadPreferences(context)

        val latitude = readPreferences.readPreference("UserInformation", "Latitude", null)
        val longitude = readPreferences.readPreference("UserInformation", "Longitude", null)

        if (latitude != null && longitude != null) {
            userLocation = LatLng(latitude.toDouble(), longitude.toDouble())
        }

        return userLocation
    }

}