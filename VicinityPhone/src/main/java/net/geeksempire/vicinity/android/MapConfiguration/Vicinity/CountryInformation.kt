/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 9/16/20 6:18 AM
 * Last modified 9/16/20 5:58 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.MapConfiguration.Vicinity

import android.content.Context
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import net.geeksempire.vicinity.android.Utils.Preferences.SavePreferences

interface CountryInformationInterface {
    fun countryNameReady(nameOfCountry: String)
}

class CountryInformation (private val context: Context) {

    val savePreferences = SavePreferences(context)

    fun getCurrentCountryName(countryIso: String, countryInformationInterface: CountryInformationInterface) {

        val firebaseDatabaseCountriesLocation: FirebaseDatabase = Firebase.database("https://vicinity-online-society-countries-information.firebaseio.com/")

        val databaseReferenceCountriesLocation = firebaseDatabaseCountriesLocation.reference
            .child("CountriesInformation")
            .child(countryIso)

        databaseReferenceCountriesLocation.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d(this@CountryInformation.javaClass.simpleName, dataSnapshot.child("Country").value.toString())

                savePreferences.savePreference("UserInformation", "Country", dataSnapshot.child("Country").value.toString())
                savePreferences.savePreference("UserInformation", "CountryCode", dataSnapshot.child("PhoneCode").value.toString())

                countryInformationInterface.countryNameReady(dataSnapshot.child("Country").value.toString())

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }

        })

    }

}