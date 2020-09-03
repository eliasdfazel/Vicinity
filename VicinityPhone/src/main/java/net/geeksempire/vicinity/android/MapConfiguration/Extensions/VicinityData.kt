/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 9/3/20 10:15 AM
 * Last modified 9/3/20 10:15 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.MapConfiguration.Extensions

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import net.geeksempire.vicinity.android.CommunicationConfiguration.Public.Endpoint.PublicCommunicationEndpoint
import net.geeksempire.vicinity.android.MapConfiguration.Map.MapsOfSociety

fun MapsOfSociety.loadVicinityData(countryName: String, locationLatitudeLongitude: LatLng) {

    val firestoreDatabase: FirebaseFirestore = Firebase.firestore

    firestoreDatabase
        .collection(PublicCommunicationEndpoint.publicCommunityEndpoint(countryName, locationLatitudeLongitude))
        .get()
        .addOnSuccessListener {
            Log.d(this@loadVicinityData.javaClass.simpleName, it.toString())

            userLatitudeLongitude?.let { userLatitudeLongitude ->

                vicinityDocument@ for (documentSnapshot in it.documents) {

                    val communityLatitude = documentSnapshot["Latitude"].toString().toDouble()
                    val communityLongitude = documentSnapshot["Longitude"].toString().toDouble()

                    if (vicinityCalculations.insideVicinity(userLatitudeLongitude, LatLng(communityLatitude, communityLongitude))) {



                        break@vicinityDocument

                    } else {



                    }

                }

            }


        }.addOnFailureListener {



        }

}