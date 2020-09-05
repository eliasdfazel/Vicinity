/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 9/5/20 11:05 AM
 * Last modified 9/5/20 11:02 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.MapConfiguration.Extensions

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import net.geeksempire.vicinity.android.CommunicationConfiguration.Public.Endpoint.PublicCommunicationEndpoint
import net.geeksempire.vicinity.android.MapConfiguration.Map.MapsOfSociety
import net.geeksempire.vicinity.android.MapConfiguration.Vicinity.Operations.VicinityData

fun MapsOfSociety.loadVicinityData(countryName: String, locationLatitudeLongitude: LatLng) {

    firestoreDatabase
        .collection(PublicCommunicationEndpoint.publicCommunityCollectionEndpoint(countryName))
        .get()
        .addOnSuccessListener {
            Log.d(this@loadVicinityData.javaClass.simpleName, it.toString())

            if (!it.isEmpty) {

                userLatitudeLongitude?.let { userLatitudeLongitude ->

                    vicinityDocument@ for (documentSnapshot in it.documents) {

                        val communityLatitude = documentSnapshot["Latitude"].toString().toDouble()
                        val communityLongitude = documentSnapshot["Longitude"].toString().toDouble()

                        if (vicinityCalculations.joinVicinity(userLatitudeLongitude, LatLng(communityLatitude, communityLongitude))) {



                            break@vicinityDocument

                        } else { /*Create New Vicinity*/

                            val vicinityData: VicinityData = VicinityData(
                                centerLatitude = userLatitudeLongitude.latitude.toString(), centerLongitude = userLatitudeLongitude.longitude.toString(),
                                countryName = countryName,  cityName = "",
                                knownAddress = "", approximateIpAddress = ""
                            )

                            createVicinity.create(
                                PublicCommunicationEndpoint.publicCommunityDocumentEndpoint(countryName, locationLatitudeLongitude),
                                vicinityData
                            )

                        }

                    }

                }

            } else {/*Create New Vicinity*/

                userLatitudeLongitude?.let { userLatitudeLongitude ->

                    val vicinityData: VicinityData = VicinityData(
                        centerLatitude = userLatitudeLongitude.latitude.toString(), centerLongitude = userLatitudeLongitude.longitude.toString(),
                        countryName = countryName,  cityName = "",
                        knownAddress = "", approximateIpAddress = ""
                    )

                    createVicinity.create(
                        PublicCommunicationEndpoint.publicCommunityDocumentEndpoint(countryName, locationLatitudeLongitude),
                        vicinityData
                    )

                }

            }

        }.addOnFailureListener {



        }

}