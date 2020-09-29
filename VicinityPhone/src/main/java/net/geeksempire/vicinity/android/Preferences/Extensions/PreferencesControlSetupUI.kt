/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 9/29/20 12:59 PM
 * Last modified 9/29/20 12:54 PM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.Preferences.Extensions

import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
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

            preferencesControlViewBinding.userDisplayName.setTextColor(getColor(R.color.dark))

            val accountViewBackground = getDrawable(R.drawable.preferences_account_view_background) as LayerDrawable
            val gradientDrawable = (accountViewBackground.findDrawableByLayerId(R.id.temporaryBackground) as GradientDrawable)
            gradientDrawable.colors = intArrayOf(getColor(R.color.dark), getColor(R.color.dark_transparent), getColor(R.color.dark_blurry_color))
            gradientDrawable.gradientType = GradientDrawable.LINEAR_GRADIENT
            accountViewBackground.findDrawableByLayerId(R.id.temporaryForeground).setTint(getColor(R.color.light))

            preferencesControlViewBinding.accountManagerView.background = accountViewBackground

        }
        ThemeType.ThemeDark -> {

            preferencesControlViewBinding.rootView.setBackgroundColor(getColor(R.color.dark))

            preferencesControlViewBinding.userDisplayName.setTextColor(getColor(R.color.light))

            val accountViewBackground = getDrawable(R.drawable.preferences_account_view_background) as LayerDrawable
            val gradientDrawable = (accountViewBackground.findDrawableByLayerId(R.id.temporaryBackground) as GradientDrawable)
            gradientDrawable.colors = intArrayOf(getColor(R.color.light), getColor(R.color.light_transparent), getColor(R.color.light_blurry_color))
            gradientDrawable.gradientType = GradientDrawable.LINEAR_GRADIENT
            accountViewBackground.findDrawableByLayerId(R.id.temporaryForeground).setTint(getColor(R.color.dark))

            preferencesControlViewBinding.accountManagerView.background = accountViewBackground

        }
    }

}