/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 9/5/20 11:05 AM
 * Last modified 9/5/20 10:31 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.Utils.System

import android.content.Context
import android.telephony.TelephonyManager
import java.util.*

class DeviceSystemInformation (private val context: Context) {

    fun getCountryIso(): String {

        var countryISO = "Undefined"

        try {
            val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            countryISO = telephonyManager.simCountryIso

            if (countryISO.length < 2) {
                countryISO = "Undefined"
            }

        } catch (e: Exception) {
            e.printStackTrace()

            countryISO = "Undefined"
        }

        return countryISO.toUpperCase(Locale.getDefault())
    }
}