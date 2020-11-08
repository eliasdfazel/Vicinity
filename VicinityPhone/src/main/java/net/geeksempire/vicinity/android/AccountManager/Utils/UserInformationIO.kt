/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 11/8/20 9:53 AM
 * Last modified 11/8/20 5:05 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.AccountManager.Utils

import android.content.Context
import com.google.android.gms.maps.model.LatLng
import net.geeksempire.vicinity.android.Invitation.Utils.InvitationConstant
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

    fun savePrivacyAgreement() {

        val savePreferences = SavePreferences(context)

        savePreferences.savePreference("UserInformation", "PrivacyAgreement", true)

    }

    fun readPrivacyAgreement() : Boolean {

        val readPreferences = ReadPreferences(context)

        return readPreferences.readPreference("UserInformation", "PrivacyAgreement", false)
    }

    /**
     * Account Type;
     * InvitationConstant.InvitationTypes.Personal
     * InvitationConstant.InvitationTypes.Business
     **/
    fun saveAccountType(accountType: String) {

        val savePreferences = SavePreferences(context)

        savePreferences.savePreference("UserInformation", "AccountType", accountType)

    }

    /**
     * Account Type;
     * InvitationConstant.InvitationTypes.Personal
     * InvitationConstant.InvitationTypes.Business
     **/
    fun readAccountType() : String {

        val readPreferences = ReadPreferences(context)

        return readPreferences.readPreference("UserInformation", "AccountType", InvitationConstant.InvitationTypes.Personal)?:InvitationConstant.InvitationTypes.Personal
    }

}