/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 9/5/20 11:05 AM
 * Last modified 9/5/20 10:31 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.MapConfiguration.Vicinity

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

interface CountryInformationInterface {
    fun countryNameReady(nameOfCountry: String)
}

class CountryInformation {

    fun getCurrentCountryName(countryIso: String, countryInformationInterface: CountryInformationInterface) {

        val firebaseDatabaseCountriesLocation: FirebaseDatabase = Firebase.database("https://vicinity-online-society-countries-information.firebaseio.com/")

        val databaseReferenceCountriesLocation = firebaseDatabaseCountriesLocation.reference
            .child("CountriesInformation")
            .child(countryIso)

        databaseReferenceCountriesLocation.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d(this@CountryInformation.javaClass.simpleName, dataSnapshot.child("Country").value.toString())

                countryInformationInterface.countryNameReady(dataSnapshot.child("Country").value.toString())

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }

        })

    }

}