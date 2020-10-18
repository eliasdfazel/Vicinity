/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 10/18/20 8:28 AM
 * Last modified 10/18/20 8:28 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.CommunicationConfiguration.HistoryConfiguration.Extensions

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import com.google.android.material.button.MaterialButton
import net.geeksempire.vicinity.android.CommunicationConfiguration.HistoryConfiguration.HistoryUI.HistoryLists
import net.geeksempire.vicinity.android.R
import net.geeksempire.vicinity.android.Utils.UI.Theme.ThemeType

fun HistoryLists.historyListsSetupUI() {

    val gradientDrawable = GradientDrawable(GradientDrawable.Orientation.TL_BR,
        intArrayOf(
            getColor(R.color.default_color_dark),
            getColor(R.color.default_color),
            getColor(R.color.default_color_bright),
            getColor(R.color.default_color_bright),
            getColor(R.color.default_color),
            getColor(R.color.default_color_dark),
        )
    )

    window.setBackgroundDrawable(gradientDrawable)

    when (overallTheme.checkThemeLightDark()) {
        ThemeType.ThemeLight -> {

            historyListsViewBinding.rootView.setBackgroundColor(getColor(R.color.light))

        }
        ThemeType.ThemeDark -> {

            historyListsViewBinding.rootView.setBackgroundColor(getColor(R.color.dark))

        }
    }

}

fun publicHistoryButtonBackgroundChange(context: Context, publicHistory: MaterialButton, privateHistory: MaterialButton) {

    val activatePublicValueAnimator = ValueAnimator.ofArgb(context.getColor(R.color.default_color_dark), context.getColor(R.color.default_color_bright))
    activatePublicValueAnimator.duration = 555
    activatePublicValueAnimator.addUpdateListener { animator ->
        val animatorValue = animator.animatedValue as Int

        publicHistory.backgroundTintList = ColorStateList.valueOf(animatorValue)

    }
    activatePublicValueAnimator.start()

    val deactivatePrivateValueAnimator = ValueAnimator.ofArgb(context.getColor(R.color.default_color_bright), context.getColor(R.color.default_color_dark))
    deactivatePrivateValueAnimator.duration = 555
    deactivatePrivateValueAnimator.addUpdateListener { animator ->
        val animatorValue = animator.animatedValue as Int

        privateHistory.backgroundTintList = ColorStateList.valueOf(animatorValue)

    }
    deactivatePrivateValueAnimator.start()

}

fun privateHistoryButtonBackgroundChange(context: Context, publicHistory: MaterialButton, privateHistory: MaterialButton) {

    val deactivatePublicValueAnimator = ValueAnimator.ofArgb(context.getColor(R.color.default_color_bright), context.getColor(R.color.default_color_dark))
    deactivatePublicValueAnimator.duration = 555
    deactivatePublicValueAnimator.addUpdateListener { animator ->
        val animatorValue = animator.animatedValue as Int

        publicHistory.backgroundTintList = ColorStateList.valueOf(animatorValue)

    }
    deactivatePublicValueAnimator.start()

    val activatePrivateValueAnimator = ValueAnimator.ofArgb(context.getColor(R.color.default_color_dark), context.getColor(R.color.default_color_bright))
    activatePrivateValueAnimator.duration = 555
    activatePrivateValueAnimator.addUpdateListener { animator ->
        val animatorValue = animator.animatedValue as Int

        privateHistory.backgroundTintList = ColorStateList.valueOf(animatorValue)

    }
    activatePrivateValueAnimator.start()

}