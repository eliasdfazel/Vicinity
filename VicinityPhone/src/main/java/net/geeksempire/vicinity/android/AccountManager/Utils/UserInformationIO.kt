/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 9/14/20 8:30 AM
 * Last modified 9/14/20 8:03 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.AccountManager.Utils

import android.content.Context
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

}