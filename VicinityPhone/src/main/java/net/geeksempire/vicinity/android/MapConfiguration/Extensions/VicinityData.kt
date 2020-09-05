/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 9/5/20 8:30 AM
 * Last modified 9/5/20 7:55 AM
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

            if (it.isEmpty) {

                userLatitudeLongitude?.let { userLatitudeLongitude ->

                    vicinityDocument@ for (documentSnapshot in it.documents) {

                        val communityLatitude = documentSnapshot["Latitude"].toString().toDouble()
                        val communityLongitude = documentSnapshot["Longitude"].toString().toDouble()

                        if (vicinityCalculations.joinVicinity(userLatitudeLongitude, LatLng(communityLatitude, communityLongitude))) {



                            break@vicinityDocument

                        } else { /*Create New Vicinity*/



                        }

                    }

                }

            } else {/*Create New Vicinity*/



            }

        }.addOnFailureListener {



        }

}