/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 9/12/20 4:38 AM
 * Last modified 9/12/20 4:25 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.Utils.RemoteTask

import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import net.geeksempire.vicinity.android.MapConfiguration.Vicinity.vicinityName
import net.geeksempire.vicinity.android.Utils.Location.LocationCheckpoint
import net.geeksempire.vicinity.android.Utils.UI.Colors.extractDominantColor
import net.geeksempire.vicinity.android.Utils.UI.NotifyUser.NotificationCreator

class CloudNotificationHandler : FirebaseMessagingService() {

    val locationCheckpoint = LocationCheckpoint()

    val notificationCreator by lazy {
        NotificationCreator(applicationContext)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(this@CloudNotificationHandler.javaClass.simpleName, "${remoteMessage.data}")

        val linkedHashMapData = remoteMessage.data

        Glide.with(applicationContext)
            .asBitmap()
            .load(linkedHashMapData["notificationLargeIcon"])
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .listener(object : RequestListener<Bitmap> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {

                    return false
                }

                override fun onResourceReady(resource: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {

                    resource?.let { bitmap ->

                        val publicCommunityIntent = Intent(linkedHashMapData["publicCommunityAction"].toString())
                        publicCommunityIntent.putExtra("publicCommunityName", linkedHashMapData["publicCommunityName"])
                        publicCommunityIntent.putExtra("fromNotification", true)

                        val publicCommunityPendingIntent: PendingIntent = PendingIntent.getActivity(applicationContext, 666, publicCommunityIntent, PendingIntent.FLAG_UPDATE_CURRENT)

                        notificationCreator.create(
                            notificationChannelId = linkedHashMapData["publicCommunityName"].toString(),
                            titleText = linkedHashMapData["selfDisplayName"].toString(),
                            contentText = linkedHashMapData["messageContent"].toString(),
                            largeIcon = bitmap,
                            notificationColor = extractDominantColor(applicationContext, bitmap),
                            notificationId = System.currentTimeMillis(),
                            locationKnownName = locationCheckpoint.knownLocationName(applicationContext, vicinityName(LatLng(linkedHashMapData["userLatitude"].toString().toDouble(), linkedHashMapData["userLongitude"].toString().toDouble()))).toString(),
                            pendingIntent = publicCommunityPendingIntent
                        )

                    }

                    return false
                }

            })
            .submit()

    }

    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)
    }
}
