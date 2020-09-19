/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 9/19/20 10:20 AM
 * Last modified 9/19/20 10:20 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.MapConfiguration.Utils

import android.graphics.drawable.Drawable
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
import com.google.android.gms.maps.model.Marker
import com.google.firebase.auth.FirebaseUser
import net.geeksempire.vicinity.android.R
import net.geeksempire.vicinity.android.Utils.UI.Images.drawableToBitmap
import net.geeksempire.vicinity.android.Utils.UI.Images.getCircularBitmapWithWhiteBorder

class MapsMarker (private val context: AppCompatActivity, private val firebaseUser: FirebaseUser?,
                  private val googleMap: GoogleMap, private val mapMarker: Marker) {

    fun updateUserMarkerLocation(locationLatitudeLongitude: LatLng) {

        Glide.with(context)
            .asDrawable()
            .load(firebaseUser?.photoUrl)
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

                            mapMarker.position = (locationLatitudeLongitude)
                            mapMarker.title = ("${firebaseUser?.displayName}")
                            mapMarker.snippet = ("${firebaseUser?.email}")
                            mapMarker.tag = firebaseUser?.uid
                            mapMarker.isDraggable = false

                            mapMarker.setIcon(bitmapDescriptorIcon)

                        }

                    }

                    return false
                }

            })
            .submit()

    }

}