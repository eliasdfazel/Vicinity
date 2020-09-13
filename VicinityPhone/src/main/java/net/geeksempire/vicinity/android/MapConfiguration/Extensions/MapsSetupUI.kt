/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 9/13/20 8:01 AM
 * Last modified 9/13/20 8:01 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.MapConfiguration.Extensions

import androidx.constraintlayout.widget.ConstraintLayout
import net.geeksempire.vicinity.android.MapConfiguration.Map.MapsOfSociety
import net.geeksempire.vicinity.android.Utils.UI.Display.statusBarHeight

fun MapsOfSociety.mapsOfSocietySetupUI() {

    val preferenceViewLayoutParams = mapsViewBinding.preferenceView.layoutParams as ConstraintLayout.LayoutParams
    preferenceViewLayoutParams.setMargins(0, preferenceViewLayoutParams.topMargin + statusBarHeight(applicationContext), 0, 0)
    mapsViewBinding.preferenceView.layoutParams = preferenceViewLayoutParams

}

fun MapsOfSociety.setClicksSetup() {

    mapsViewBinding.preferenceView.setOnClickListener {



    }

}