/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 9/29/20 6:54 AM
 * Last modified 9/29/20 6:53 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.CommunicationConfiguration.Public.Extensions

import androidx.constraintlayout.widget.ConstraintLayout
import net.geeksempire.vicinity.android.CommunicationConfiguration.Public.PublicCommunityUI.PublicCommunity
import net.geeksempire.vicinity.android.R
import net.geeksempire.vicinity.android.Utils.UI.Display.DpToInteger
import net.geeksempire.vicinity.android.Utils.UI.Display.navigationBarHeight
import net.geeksempire.vicinity.android.Utils.UI.Display.statusBarHeight
import net.geeksempire.vicinity.android.Utils.UI.Theme.ThemeType

fun PublicCommunity.publicCommunitySetupUI() {

    when (overallTheme.checkThemeLightDark()) {
        ThemeType.ThemeLight -> {

            publicCommunityViewBinding.rootView.setBackgroundColor(getColor(R.color.light))

            publicCommunityViewBinding.messageContentWrapper.setBackgroundColor(getColor(R.color.light))

            publicCommunityViewBinding.textMessageContentLayout.boxBackgroundColor = getColor(R.color.white)

            publicCommunityViewBinding.textMessageContentView.setTextColor(getColor(R.color.dark))

        }
        ThemeType.ThemeDark -> {

            publicCommunityViewBinding.rootView.setBackgroundColor(getColor(R.color.dark))

            publicCommunityViewBinding.messageContentWrapper.setBackgroundColor(getColor(R.color.dark))

            publicCommunityViewBinding.textMessageContentLayout.boxBackgroundColor = getColor(R.color.black)

            publicCommunityViewBinding.textMessageContentView.setTextColor(getColor(R.color.light))

        }
    }

    publicCommunityViewBinding.scrollWrapper.setPadding(0, publicCommunityViewBinding.scrollWrapper.paddingTop + statusBarHeight(applicationContext), 0, 0)

    publicCommunityViewBinding.messageContentWrapper.post {
        val scrollWrapperLayoutParams = publicCommunityViewBinding.nestedScrollView.layoutParams as ConstraintLayout.LayoutParams
        scrollWrapperLayoutParams.setMargins(0, 0, 0, navigationBarHeight(applicationContext) + publicCommunityViewBinding.messageContentWrapper.height)
        publicCommunityViewBinding.nestedScrollView.layoutParams = scrollWrapperLayoutParams
    }

    val messageContentWrapperLayoutParams = publicCommunityViewBinding.messageContentWrapper.layoutParams as ConstraintLayout.LayoutParams
    messageContentWrapperLayoutParams.setMargins(0, 0, 0, navigationBarHeight(applicationContext) + DpToInteger(applicationContext, 13))
    publicCommunityViewBinding.messageContentWrapper.layoutParams = messageContentWrapperLayoutParams

}