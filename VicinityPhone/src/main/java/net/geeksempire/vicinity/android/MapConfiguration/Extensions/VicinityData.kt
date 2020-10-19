/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 10/19/20 10:59 AM
 * Last modified 10/19/20 10:59 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.MapConfiguration.Extensions

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FieldValue
import net.geeksempire.vicinity.android.AccountManager.DataStructure.UserInformationData
import net.geeksempire.vicinity.android.CommunicationConfiguration.Public.Endpoint.PublicCommunicationEndpoint
import net.geeksempire.vicinity.android.MapConfiguration.Map.MapsOfSociety
import net.geeksempire.vicinity.android.MapConfiguration.Vicinity.DataStructure.VicinityData
import net.geeksempire.vicinity.android.MapConfiguration.Vicinity.Operations.VicinityUserInformation
import net.geeksempire.vicinity.android.Utils.Location.LocationCheckpoint

fun MapsOfSociety.loadVicinityData(countryName: String, userLocationLatitudeLongitude: LatLng) {

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

                        if (vicinityCalculations.joinedVicinity(userLatitudeLongitude, LatLng(communityLatitude, communityLongitude))) {

                            firebaseUser?.let {

                                val vicinityData: VicinityData = VicinityData(
                                    centerLatitude = communityLatitude.toString(), centerLongitude = communityLongitude.toString(),
                                    countryName = countryName,
                                    cityName = LocationCheckpoint.LOCATION_CITY_NAME,
                                    knownAddress = LocationCheckpoint.LOCATION_INFORMATION_DETAIL,
                                    approximateIpAddress = LocationCheckpoint.LOCATION_KNOWN_IP
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

                            val vicinityData: VicinityData = VicinityData(
                                centerLatitude = userLatitudeLongitude.latitude.toString(), centerLongitude = userLatitudeLongitude.longitude.toString(),
                                countryName = countryName,
                                cityName = LocationCheckpoint.LOCATION_CITY_NAME,
                                knownAddress = LocationCheckpoint.LOCATION_INFORMATION_DETAIL,
                                approximateIpAddress = LocationCheckpoint.LOCATION_KNOWN_IP
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
                                PublicCommunicationEndpoint.publicCommunityDocumentEndpoint(countryName, userLocationLatitudeLongitude),
                                vicinityData,
                                userInformationData,
                                userLatitudeLongitude
                            )

                        }

                    }

                }

            } else {/*Create New Vicinity*/

                userLatitudeLongitude?.let { userLatitudeLongitude ->

                    firebaseUser?.let {

                        val vicinityData: VicinityData = VicinityData(
                            centerLatitude = userLatitudeLongitude.latitude.toString(), centerLongitude = userLatitudeLongitude.longitude.toString(),
                            countryName = countryName,
                            cityName = LocationCheckpoint.LOCATION_CITY_NAME,
                            knownAddress = LocationCheckpoint.LOCATION_INFORMATION_DETAIL,
                            approximateIpAddress = LocationCheckpoint.LOCATION_KNOWN_IP
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
                            PublicCommunicationEndpoint.publicCommunityDocumentEndpoint(countryName, userLocationLatitudeLongitude),
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