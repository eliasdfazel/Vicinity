/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 10/5/20 8:58 AM
 * Last modified 10/5/20 8:07 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.Utils.UI.Images

import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View

fun takeViewSnapshot(view: View) : Bitmap {

    val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)

    val canvas = Canvas(bitmap)
    view.draw(canvas)

    return bitmap
}