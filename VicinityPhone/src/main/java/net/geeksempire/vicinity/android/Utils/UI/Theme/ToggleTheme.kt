/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 9/21/20 12:13 PM
 * Last modified 9/21/20 12:09 PM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.Utils.UI.Theme

import androidx.appcompat.app.AppCompatActivity
import net.geeksempire.vicinity.android.Preferences.PreferencesControl
import net.geeksempire.vicinity.android.databinding.PreferencesControlViewBinding

class ToggleTheme (private val context: AppCompatActivity, private val preferencesControlViewBinding: PreferencesControlViewBinding) {

    val overallTheme = OverallTheme(context)

    fun initialThemeToggleAction() {

        when (overallTheme.checkThemeLightDark()) {
            ThemeType.ThemeLight -> {
                preferencesControlViewBinding.toggleThemeView.apply {
                    speed = 3.33f
                    setMinAndMaxFrame(0, 7)
                }.playAnimation()
            }
            ThemeType.ThemeDark -> {
                preferencesControlViewBinding.toggleThemeView.apply {
                    speed = -3.33f
                    setMinAndMaxFrame(90, 99)
                }.playAnimation()
            }
        }

        toggleLightDarkTheme()

    }

    private fun toggleLightDarkTheme() {

        preferencesControlViewBinding.toggleThemeView.setOnClickListener { view ->

            preferencesControlViewBinding.toggleThemeView.also {

                when (overallTheme.checkThemeLightDark()) {
                    ThemeType.ThemeLight -> {

                        it.speed = 1.130f
                        it.setMinAndMaxFrame(7, 90)

                        if (!it.isAnimating) {
                            it.playAnimation()
                        }

                        overallTheme.saveThemeLightDark(ThemeType.ThemeDark)

                    }
                    ThemeType.ThemeDark -> {

                        it.speed = -1.130f
                        it.setMinAndMaxFrame(7, 90)

                        if (!it.isAnimating) {
                            it.playAnimation()
                        }

                        overallTheme.saveThemeLightDark(ThemeType.ThemeLight)

                    }
                }

            }

            when(context) {
                is PreferencesControl -> {
                    (context as PreferencesControl).preferencesLiveData.toggleTheme.postValue(true)
                }
            }

        }

        preferencesControlViewBinding.root.setOnClickListener {



        }

    }

}