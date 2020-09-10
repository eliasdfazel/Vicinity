/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 9/10/20 10:43 AM
 * Last modified 9/10/20 10:43 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.Utils.RemoteTask

import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import net.geeksempire.vicinity.android.Utils.UI.NotifyUser.NotificationCreator

class CloudNotificationHandler : FirebaseMessagingService() {

    val notificationCreator by lazy {
        NotificationCreator(applicationContext)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        val linkedHashMapData = remoteMessage.data

        Glide.with(applicationContext)
            .asBitmap()
            .load("")
            .listener(object : RequestListener<Bitmap> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {

                    return false
                }

                override fun onResourceReady(resource: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {

                    resource?.let { bitmap ->

                        val intent: Intent = Intent(linkedHashMapData["ChatAction"].toString())
                        intent.putExtra("CommunityLocation", communityLocation)
                        intent.putExtra("ChatName", chatName)
                        intent.putExtra("CommunityNickname", communityNickname)
                        if (targetUID != null) {
                            intent.putExtra("targetUID", targetUID)
                        }
                        if (targetRegistrationToken != null) {
                            intent.putExtra("targetRegistrationToken", targetRegistrationToken)
                        }
                        intent.putExtra("EarthHemisphere", earthHemisphere)
                        intent.putExtra("CountryName", countryName)
                        intent.putExtra("CityName", cityName)
                        intent.putExtra("fromNotification", true)

                        val pendingIntent: PendingIntent = PendingIntent.getActivity(applicationContext, 666, intent, PendingIntent.FLAG_UPDATE_CURRENT)

                        notificationCreator.create(
                            notificationChannelId = "",
                            titleText = "",
                            contentText = "",
                            largeIcon = bitmap,
                            notificationColor = 0,
                            notificationId = System.currentTimeMillis(),
                            locationKnownName = "",
                            pendingIntent = pendingIntent
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
