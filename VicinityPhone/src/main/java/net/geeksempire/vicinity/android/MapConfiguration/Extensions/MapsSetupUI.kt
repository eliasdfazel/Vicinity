/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 9/15/20 7:09 AM
 * Last modified 9/15/20 6:08 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.MapConfiguration.Extensions

import android.app.ActivityOptions
import android.content.Intent
import androidx.constraintlayout.widget.ConstraintLayout
import net.geeksempire.vicinity.android.MapConfiguration.Map.MapsOfSociety
import net.geeksempire.vicinity.android.Preferences.PreferencesControl
import net.geeksempire.vicinity.android.Utils.UI.Display.statusBarHeight
import kotlin.math.roundToInt

fun MapsOfSociety.mapsOfSocietySetupUI() {

    val preferenceViewLayoutParams = mapsViewBinding.preferenceView.layoutParams as ConstraintLayout.LayoutParams
    preferenceViewLayoutParams.setMargins(0, preferenceViewLayoutParams.topMargin + statusBarHeight(applicationContext), 0, 0)
    mapsViewBinding.preferenceView.layoutParams = preferenceViewLayoutParams

    clicksSetup()

}

fun MapsOfSociety.clicksSetup() {

    mapsViewBinding.preferenceView.setOnClickListener {

        startActivity(Intent(applicationContext, PreferencesControl::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }, ActivityOptions.makeScaleUpAnimation(mapsViewBinding.preferenceView, mapsViewBinding.preferenceView.x.roundToInt(), mapsViewBinding.preferenceView.y.roundToInt(), mapsViewBinding.preferenceView.width, mapsViewBinding.preferenceView.height).toBundle())

    }

}