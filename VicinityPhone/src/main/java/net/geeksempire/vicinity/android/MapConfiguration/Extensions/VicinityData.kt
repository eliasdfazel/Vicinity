/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 9/6/20 8:36 AM
 * Last modified 9/6/20 8:35 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.MapConfiguration.Extensions

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FieldValue
import net.geeksempire.vicinity.android.AccountManager.Data.UserInformationData
import net.geeksempire.vicinity.android.CommunicationConfiguration.Public.Endpoint.PublicCommunicationEndpoint
import net.geeksempire.vicinity.android.MapConfiguration.Map.MapsOfSociety
import net.geeksempire.vicinity.android.MapConfiguration.Vicinity.Operations.VicinityData
import net.geeksempire.vicinity.android.MapConfiguration.Vicinity.Operations.VicinityUserInformation

fun MapsOfSociety.loadVicinityData(countryName: String, locationLatitudeLongitude: LatLng) {

    firestoreDatabase
        .collection(PublicCommunicationEndpoint.publicCommunityCollectionEndpoint(countryName))
        .get()
        .addOnSuccessListener {
            Log.d(this@loadVicinityData.javaClass.simpleName, it.toString())

            if (!it.isEmpty) {

                userLatitudeLongitude?.let { userLatitudeLongitude ->

                    vicinityDocument@ for (documentSnapshot in it.documents) {

                        val communityLatitude = documentSnapshot["centerLatitude"].toString().toDouble()
                        val communityLongitude = documentSnapshot["centerLongitude"].toString().toDouble()

                        if (vicinityCalculations.joinVicinity(userLatitudeLongitude, LatLng(communityLatitude, communityLongitude))) {

                            firebaseUser?.let {

                                val vicinityData: VicinityData = VicinityData(
                                    centerLatitude = userLatitudeLongitude.latitude.toString(), centerLongitude = userLatitudeLongitude.longitude.toString(),
                                    countryName = countryName,  cityName = "",
                                    knownAddress = "", approximateIpAddress = ""
                                )

                                val userInformationData: UserInformationData = UserInformationData(
                                    userIdentification = firebaseUser.uid,
                                    userEmailAddress = firebaseUser.email.toString(),
                                    userDisplayName = firebaseUser.displayName.toString(),
                                    userProfileImage = firebaseUser.photoUrl.toString(),
                                    userLatitude = userLatitudeLongitude.latitude.toString(), userLongitude = userLatitudeLongitude.longitude.toString(),
                                    userState = "true",
                                    userLastSignIn = FieldValue.serverTimestamp()
                                )

                                joinVicinity.join(
                                    PublicCommunicationEndpoint.publicCommunityDocumentEndpoint(countryName, LatLng(communityLatitude, communityLongitude)),
                                    vicinityData,
                                    userInformationData,
                                    userLatitudeLongitude
                                )

                                val vicinityUserInformation: VicinityUserInformation = VicinityUserInformation(firestoreDatabase, PublicCommunicationEndpoint.publicCommunityDocumentEndpoint(countryName, LatLng(communityLatitude, communityLongitude)))

                                vicinityUserInformation.showAllVicinityUsers(this@loadVicinityData, readyGoogleMap, firebaseUser)

                            }


                            break@vicinityDocument

                        } else { /*Create New Vicinity*/

                            firebaseUser?.let {

                                val vicinityData: VicinityData = VicinityData(
                                    centerLatitude = userLatitudeLongitude.latitude.toString(), centerLongitude = userLatitudeLongitude.longitude.toString(),
                                    countryName = countryName,  cityName = "",
                                    knownAddress = "", approximateIpAddress = ""
                                )

                                val userInformationData: UserInformationData = UserInformationData(
                                    userIdentification = firebaseUser.uid,
                                    userEmailAddress = firebaseUser.email.toString(),
                                    userDisplayName = firebaseUser.displayName.toString(),
                                    userProfileImage = firebaseUser.photoUrl.toString(),
                                    userLatitude = userLatitudeLongitude.latitude.toString(), userLongitude = userLatitudeLongitude.longitude.toString(),
                                    userState = "true",
                                    userLastSignIn = FieldValue.serverTimestamp(),
                                    userJointDate = FieldValue.serverTimestamp()
                                )

                                createVicinity.create(
                                    PublicCommunicationEndpoint.publicCommunityDocumentEndpoint(countryName, locationLatitudeLongitude),
                                    vicinityData,
                                    userInformationData,
                                    userLatitudeLongitude
                                )
                            }

                        }

                    }

                }

            } else {/*Create New Vicinity*/

                userLatitudeLongitude?.let { userLatitudeLongitude ->

                    firebaseUser?.let {

                        val vicinityData: VicinityData = VicinityData(
                            centerLatitude = userLatitudeLongitude.latitude.toString(), centerLongitude = userLatitudeLongitude.longitude.toString(),
                            countryName = countryName,  cityName = "",
                            knownAddress = "", approximateIpAddress = ""
                        )

                        val userInformationData: UserInformationData = UserInformationData(
                            userIdentification = firebaseUser.uid,
                            userEmailAddress = firebaseUser.email.toString(),
                            userDisplayName = firebaseUser.displayName.toString(),
                            userProfileImage = firebaseUser.photoUrl.toString(),
                            userLatitude = userLatitudeLongitude.latitude.toString(), userLongitude = userLatitudeLongitude.longitude.toString(),
                            userState = "true",
                            userLastSignIn = FieldValue.serverTimestamp(),
                            userJointDate = FieldValue.serverTimestamp()
                        )

                        createVicinity.create(
                            PublicCommunicationEndpoint.publicCommunityDocumentEndpoint(countryName, locationLatitudeLongitude),
                            vicinityData,
                            userInformationData,
                            userLatitudeLongitude
                        )
                    }

                }

            }

        }.addOnFailureListener {



        }

}