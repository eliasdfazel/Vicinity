/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 9/29/20 6:00 AM
 * Last modified 9/29/20 5:54 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.WebView

import android.webkit.JavascriptInterface

/**
 * Android
 **/
class WebInterface (private val context: BuiltInWebView) {

    /**
     * ThemeType
     * 0 = Light
     * 1 = Dark
     **/
    @JavascriptInterface
    fun getThemeColor() : Int {

        return context.overallTheme.checkThemeLightDark()
    }

}