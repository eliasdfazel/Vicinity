/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 9/8/20 10:55 AM
 * Last modified 9/8/20 10:55 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.CommunicationConfiguration.Public.Extensions

import androidx.constraintlayout.widget.ConstraintLayout
import net.geeksempire.vicinity.android.CommunicationConfiguration.Public.PublicCommunityUI.PublicCommunity
import net.geeksempire.vicinity.android.Utils.UI.Display.navigationBarHeight
import net.geeksempire.vicinity.android.Utils.UI.Display.statusBarHeight

fun PublicCommunity.publicCommunitySetUpUI() {

    publicCommunityViewBinding.scrollWrapper.setPadding(0, publicCommunityViewBinding.scrollWrapper.paddingTop + statusBarHeight(applicationContext), 0, 0)

    val sendButtonLayoutParams = publicCommunityViewBinding.sendMessageView.layoutParams as ConstraintLayout.LayoutParams
    sendButtonLayoutParams.setMargins(0, 0, 0, navigationBarHeight(applicationContext))
    publicCommunityViewBinding.sendMessageView.layoutParams = sendButtonLayoutParams

}