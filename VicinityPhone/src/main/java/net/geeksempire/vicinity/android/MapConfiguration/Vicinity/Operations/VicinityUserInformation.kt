/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 9/8/20 10:55 AM
 * Last modified 9/8/20 8:18 AM
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
import com.google.firebase.firestore.FirebaseFirestore
import net.geeksempire.vicinity.android.AccountManager.DataStructure.UserInformationData
import net.geeksempire.vicinity.android.AccountManager.UserInformation
import net.geeksempire.vicinity.android.R
import net.geeksempire.vicinity.android.Utils.UI.Images.drawableToBitmap
import net.geeksempire.vicinity.android.Utils.UI.Images.getCircularBitmapWithWhiteBorder

class VicinityUserInformation (private val firestoreDatabase: FirebaseFirestore, private val vicinityDatabasePath: String) {

    fun add(userInformationData: UserInformationData) {

        firestoreDatabase
            .document(UserInformation.uniqueUserInformationDatabasePath(vicinityDatabasePath, userInformationData.userIdentification))
            .set(userInformationData)
            .addOnSuccessListener {
                Log.d(this@VicinityUserInformation.javaClass.simpleName, "User Added To ${vicinityDatabasePath}")

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

                    val userIdentifier = documentSnapshot["userIdentification"].toString()

                    if (userIdentifier != firebaseUser.uid) {

                        val userLatitude = documentSnapshot["userLatitude"].toString().toDouble()
                        val userLongitude = documentSnapshot["userLongitude"].toString().toDouble()

                        val userMapMarker = googleMap.addMarker(
                            MarkerOptions()
                                .position(LatLng(userLatitude, userLongitude))
                                .title(documentSnapshot["userDisplayName"].toString())
                                .snippet(documentSnapshot["userEmailAddress"].toString())
                        )
                        userMapMarker.isDraggable = false

                        Glide.with(context)
                            .asDrawable()
                            .load(documentSnapshot["userProfileImage"].toString())
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
                                            userMapMarker.title = documentSnapshot["userDisplayName"].toString()
                                            userMapMarker.snippet = documentSnapshot["userEmailAddress"].toString()
                                            userMapMarker.tag = documentSnapshot["userIdentification"].toString()
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

}