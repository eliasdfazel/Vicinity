/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 9/21/20 12:13 PM
 * Last modified 9/21/20 12:10 PM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.Preferences.Extensions

import androidx.constraintlayout.widget.ConstraintLayout
import net.geeksempire.vicinity.android.Preferences.PreferencesControl
import net.geeksempire.vicinity.android.R
import net.geeksempire.vicinity.android.Utils.UI.Display.navigationBarHeight
import net.geeksempire.vicinity.android.Utils.UI.Display.statusBarHeight
import net.geeksempire.vicinity.android.Utils.UI.Theme.ThemeType
import net.geeksempire.vicinity.android.Utils.UI.Theme.ToggleTheme

fun PreferencesControl.preferencesControlSetupUI() {

    val toggleTheme = ToggleTheme(this@preferencesControlSetupUI, preferencesControlViewBinding)
    toggleTheme.initialThemeToggleAction()

    toggleLightDark()

    preferencesControlViewBinding.rootContainer.setPadding(0, preferencesControlViewBinding.rootContainer.paddingTop + statusBarHeight(applicationContext), 0, 0)

    val rootContainerLayoutParams = preferencesControlViewBinding.rootContainer.layoutParams as ConstraintLayout.LayoutParams
    rootContainerLayoutParams.setMargins(0, 0, 0, navigationBarHeight(applicationContext))
    preferencesControlViewBinding.rootContainer.layoutParams = rootContainerLayoutParams

}

fun PreferencesControl.toggleLightDark() {

    when (overallTheme.checkThemeLightDark()) {
        ThemeType.ThemeLight -> {

            preferencesControlViewBinding.rootView.setBackgroundColor(getColor(R.color.light))

        }
        ThemeType.ThemeDark -> {

            preferencesControlViewBinding.rootView.setBackgroundColor(getColor(R.color.dark))

        }
    }

}