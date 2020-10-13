/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 10/13/20 6:29 AM
 * Last modified 10/13/20 6:28 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.CommunicationConfiguration.Private.Extensions

import androidx.constraintlayout.widget.ConstraintLayout
import net.geeksempire.vicinity.android.CommunicationConfiguration.Private.PrivateMessengerUI.PrivateMessenger
import net.geeksempire.vicinity.android.R
import net.geeksempire.vicinity.android.Utils.UI.Display.DpToInteger
import net.geeksempire.vicinity.android.Utils.UI.Display.navigationBarHeight
import net.geeksempire.vicinity.android.Utils.UI.Display.statusBarHeight
import net.geeksempire.vicinity.android.Utils.UI.Theme.ThemeType

fun PrivateMessenger.privateMessengerSetupUI() {

    privateMessengerViewBinding.messageContentWrapper.bringToFront()

    when (overallTheme.checkThemeLightDark()) {
        ThemeType.ThemeLight -> {

            privateMessengerViewBinding.rootView.setBackgroundColor(getColor(R.color.light))

            privateMessengerViewBinding.messageContentBlurryBackground.setBackgroundColor(getColor(R.color.light_blurry_color))

            privateMessengerViewBinding.textMessageContentLayout.boxBackgroundColor = getColor(R.color.white)

            privateMessengerViewBinding.textMessageContentView.setTextColor(getColor(R.color.dark))

        }
        ThemeType.ThemeDark -> {

            privateMessengerViewBinding.rootView.setBackgroundColor(getColor(R.color.dark))

            privateMessengerViewBinding.messageContentBlurryBackground.setBackgroundColor(getColor(R.color.dark_blurry_color))

            privateMessengerViewBinding.textMessageContentLayout.boxBackgroundColor = getColor(R.color.black)

            privateMessengerViewBinding.textMessageContentView.setTextColor(getColor(R.color.light))

        }
    }

    privateMessengerViewBinding.vicinityFriendName.post {
        val vicinityKnownNameLayoutParams = privateMessengerViewBinding.vicinityFriendName.layoutParams as ConstraintLayout.LayoutParams
        vicinityKnownNameLayoutParams.setMargins(
            vicinityKnownNameLayoutParams.marginStart,
            statusBarHeight(applicationContext) + vicinityKnownNameLayoutParams.topMargin,
            vicinityKnownNameLayoutParams.marginEnd,
            vicinityKnownNameLayoutParams.topMargin
        )
        privateMessengerViewBinding.vicinityFriendName.layoutParams = vicinityKnownNameLayoutParams
    }

    privateMessengerViewBinding.scrollWrapper.setPadding(0, privateMessengerViewBinding.scrollWrapper.paddingTop + statusBarHeight(applicationContext), 0, 0)

    privateMessengerViewBinding.messageContentWrapper.post {
        val scrollWrapperLayoutParams = privateMessengerViewBinding.nestedScrollView.layoutParams as ConstraintLayout.LayoutParams
        scrollWrapperLayoutParams.setMargins(0, 0, 0, navigationBarHeight(applicationContext) + privateMessengerViewBinding.messageContentWrapper.height)
        privateMessengerViewBinding.nestedScrollView.layoutParams = scrollWrapperLayoutParams
    }

    val messageContentWrapperLayoutParams = privateMessengerViewBinding.messageContentWrapper.layoutParams as ConstraintLayout.LayoutParams
    messageContentWrapperLayoutParams.setMargins(0, 0, 0, navigationBarHeight(applicationContext) + DpToInteger(applicationContext, 13))
    privateMessengerViewBinding.messageContentWrapper.layoutParams = messageContentWrapperLayoutParams

}