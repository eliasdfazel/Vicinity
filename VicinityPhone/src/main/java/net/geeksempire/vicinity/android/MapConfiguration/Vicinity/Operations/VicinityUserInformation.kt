/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 10/18/20 9:14 AM
 * Last modified 10/18/20 9:10 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.MapConfiguration.Vicinity.Operations

import android.graphics.drawable.Drawable
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import net.geeksempire.vicinity.android.AccountManager.DataStructure.UserInformationData
import net.geeksempire.vicinity.android.AccountManager.DataStructure.UserInformationDataStructure
import net.geeksempire.vicinity.android.AccountManager.DataStructure.UserInformationVicinityArchiveData
import net.geeksempire.vicinity.android.AccountManager.Utils.UserInformation
import net.geeksempire.vicinity.android.MapConfiguration.Vicinity.DataStructure.VicinityData
import net.geeksempire.vicinity.android.MapConfiguration.Vicinity.vicinityName
import net.geeksempire.vicinity.android.R
import net.geeksempire.vicinity.android.Utils.Location.LocationCheckpoint
import net.geeksempire.vicinity.android.Utils.UI.Images.drawableToBitmap
import net.geeksempire.vicinity.android.Utils.UI.Images.getCircularBitmapWithWhiteBorder

class VicinityUserInformation (private val firestoreDatabase: FirebaseFirestore, private val vicinityDatabasePath: String) {

    fun add(userInformationData: UserInformationData, vicinityData: VicinityData) {

        firestoreDatabase
            .document(UserInformation.uniqueUserInformationDatabasePath(vicinityDatabasePath, userInformationData.userIdentification))
            .set(userInformationData)
            .addOnSuccessListener {
                Log.d(this@VicinityUserInformation.javaClass.simpleName, "User Added To ${vicinityDatabasePath}")

                val firebaseUser = Firebase.auth.currentUser

                firebaseUser?.let {

                    val userInformationArchiveData = UserInformationVicinityArchiveData(
                        vicinityCountry = vicinityData.countryName,
                        vicinityName = vicinityName(LatLng(vicinityData.centerLatitude.toDouble(), vicinityData.centerLongitude.toDouble())),
                        vicinityKnownName = LocationCheckpoint.LOCATION_KNOWN_NAME.toString(),
                        vicinityLatitude = vicinityData.centerLatitude,
                        vicinityLongitude = vicinityData.centerLongitude,
                        lastLatitude = userInformationData.userLatitude,
                        lastLongitude = userInformationData.userLongitude,
                    )

                    firestoreDatabase
                        .document(UserInformation.userVicinityArchiveDatabasePath(firebaseUser.uid, vicinityName(LatLng(vicinityData.centerLatitude.toDouble(), vicinityData.centerLongitude.toDouble()))))
                        .set(userInformationArchiveData)
                        .addOnSuccessListener {



                        }.addOnFailureListener {



                        }

                }

            }
            .addOnFailureListener {
                Log.d(this@VicinityUserInformation.javaClass.simpleName, "Failed To Add User")

            }

    }

    fun showAllVicinityUsers(context: AppCompatActivity, googleMap: GoogleMap, firebaseUser: FirebaseUser) {

        firestoreDatabase
            .collection(UserInformation.allUsersInformationDatabasePath(vicinityDatabasePath))
            .get()
            .addOnSuccessListener {

                vicinityDocument@ for (documentSnapshot in it.documents) {
                    Log.d(this@VicinityUserInformation.javaClass.simpleName, "${documentSnapshot[UserInformationDataStructure.userEmailAddress]}")

                    val userIdentifier = documentSnapshot[UserInformationDataStructure.userIdentification].toString()

                    if (userIdentifier != firebaseUser.uid) {

                        val userLatitude = documentSnapshot[UserInformationDataStructure.userLatitude].toString().toDouble()
                        val userLongitude = documentSnapshot[UserInformationDataStructure.userLongitude].toString().toDouble()

                        val userMapMarker = googleMap.addMarker(
                            MarkerOptions()
                                .position(LatLng(userLatitude, userLongitude))
                                .title(documentSnapshot[UserInformationDataStructure.userDisplayName].toString())
                                .snippet(documentSnapshot[UserInformationDataStructure.userEmailAddress].toString())
                        )
                        userMapMarker.isDraggable = false

                        Glide.with(context)
                            .asDrawable()
                            .load(documentSnapshot[UserInformationDataStructure.userProfileImage].toString())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .listener(object : RequestListener<Drawable> {

                                override fun onLoadFailed(glideException: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {

                                    return false
                                }

                                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {

                                    context.runOnUiThread {

                                        resource?.let {

                                            val resourcesBitmap = getCircularBitmapWithWhiteBorder(drawableToBitmap(it), 7, context.getColor(R.color.default_color_light))
                                            val bitmapDescriptorIcon: BitmapDescriptor = BitmapDescriptorFactory.fromBitmap(resourcesBitmap)

                                            userMapMarker.position = (LatLng(userLatitude, userLongitude))
                                            userMapMarker.title = documentSnapshot[UserInformationDataStructure.userDisplayName].toString()
                                            userMapMarker.snippet = documentSnapshot[UserInformationDataStructure.userEmailAddress].toString()
                                            userMapMarker.tag = documentSnapshot[UserInformationDataStructure.userIdentification].toString()
                                            userMapMarker.isDraggable = false
                                            userMapMarker.setIcon(bitmapDescriptorIcon)

                                        }

                                    }

                                    return false
                                }

                            })
                            .submit()

                    }

                }

            }.addOnFailureListener {

            }

    }

    fun updateLocation(userIdentification: String, userLatitude: String, userLongitude: String, currentVicinityName: String, currentVicinityLocation: LatLng) {

        firestoreDatabase
            .document(UserInformation.uniqueUserInformationDatabasePath(vicinityDatabasePath, userIdentification))
            .update(
                "userLatitude", userLatitude,
                "userLongitude", userLongitude,
            ).addOnSuccessListener {
                Log.d(this@VicinityUserInformation.javaClass.simpleName, "User Location In Community Updated ${vicinityDatabasePath}")

                firestoreDatabase
                    .document(UserInformation.userVicinityArchiveDatabasePath(userIdentification, currentVicinityName))
                    .update(
                        "vicinityKnownName", LocationCheckpoint.LOCATION_KNOWN_NAME.toString(),
                        "lastLatitude", userLatitude,
                        "lastLongitude", userLongitude,
                        "vicinityLatitude", currentVicinityLocation.latitude,
                        "vicinityLongitude", currentVicinityLocation.longitude
                    ).addOnSuccessListener {
                        Log.d(this@VicinityUserInformation.javaClass.simpleName, "User Location In Profile Updated ${vicinityDatabasePath}")


                    }.addOnFailureListener {



                    }

            }.addOnFailureListener {



            }

    }

}