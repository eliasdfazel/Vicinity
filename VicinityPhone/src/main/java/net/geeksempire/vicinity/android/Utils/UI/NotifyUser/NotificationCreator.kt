/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 9/12/20 4:40 AM
 * Last modified 9/12/20 4:40 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.Utils.UI.NotifyUser

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.Html
import net.geeksempire.vicinity.android.R
import net.geeksempire.vicinity.android.Utils.UI.Images.getCircularBitmapWithWhiteBorder
import java.util.*

class NotificationCreator (private val context: Context) {

    fun create(notificationChannelId: String,
               titleText: String, contentText: String, largeIcon: Bitmap, notificationColor: Int, notificationId: Long,
               locationKnownName: String?,
               pendingIntent: PendingIntent?) {

        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(113,VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(113)
        }

        val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationBuilder = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Notification.Builder(context, notificationChannelId)
        } else {
            Notification.Builder(context)
        }

        notificationBuilder.setContentTitle(
            Html.fromHtml(
                "<big><b>" + titleText + "</b></big> | " + "<small>" + locationKnownName + "</small>",
                Html.FROM_HTML_MODE_LEGACY
            )
        )
        notificationBuilder.setContentText(
            Html.fromHtml(
                "" + contentText + "",
                Html.FROM_HTML_MODE_LEGACY
            )
        )
        notificationBuilder.setTicker(context.resources.getString(R.string.applicationName))
        notificationBuilder.setSmallIcon(R.drawable.world_map_dots)
        notificationBuilder.setLargeIcon(getCircularBitmapWithWhiteBorder(largeIcon, 0, Color.TRANSPARENT))
        notificationBuilder.setAutoCancel(true)
        notificationBuilder.setColor(notificationColor)

        if (pendingIntent != null) {
            notificationBuilder.setContentIntent(pendingIntent)

            locationKnownName?.let {
                val builderActionNotification = Notification.Action.Builder(null, locationKnownName.toLowerCase(Locale.getDefault()), pendingIntent)
                notificationBuilder.addAction(builderActionNotification.build())
            }

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationBuilder.setChannelId(notificationChannelId)

            val notificationChannel = NotificationChannel(notificationChannelId, context.getString(R.string.applicationName), NotificationManager.IMPORTANCE_MIN)

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = notificationColor
            notificationChannel.enableVibration(true)
            notificationChannel.description = locationKnownName
            notificationManager.createNotificationChannel(notificationChannel)
        }

        notificationManager.notify(notificationId.toInt(), notificationBuilder.build())
    }

}