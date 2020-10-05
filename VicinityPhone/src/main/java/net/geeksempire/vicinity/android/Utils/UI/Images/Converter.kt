/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 10/5/20 8:58 AM
 * Last modified 10/5/20 8:12 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.Utils.UI.Images

import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.VectorDrawable
import java.io.ByteArrayOutputStream

fun drawableToBitmap(drawable: Drawable): Bitmap {


    return when (drawable) {
        is VectorDrawable -> {

            val vectorDrawable = drawable

            val bitmap = Bitmap.createBitmap(
                vectorDrawable.intrinsicWidth,
                vectorDrawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
            vectorDrawable.draw(canvas)

            bitmap

        }
        is BitmapDrawable -> {
            val bitmapDrawable = drawable

            bitmapDrawable.bitmap

        }
        is LayerDrawable -> {

            val layerDrawable = drawable

            val bitmap = Bitmap.createBitmap(
                layerDrawable.intrinsicWidth,
                layerDrawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            layerDrawable.setBounds(0, 0, canvas.width, canvas.height)
            layerDrawable.draw(canvas)

            bitmap

        }
        else -> {

            val bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)

            bitmap
        }
    }
}

fun drawableToByteArray(drawable: Drawable?) : ByteArray? {

    var imageByteArray: ByteArray? = null

    drawable?.let {

        val byteArrayOutputStream = ByteArrayOutputStream()

        val bitmap = (drawable as BitmapDrawable).bitmap
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)

        imageByteArray = byteArrayOutputStream.toByteArray()

    }

    return imageByteArray
}

fun layerDrawableToByteArray(layerDrawable: LayerDrawable?) : ByteArray? {

    var imageByteArray: ByteArray? = null

    layerDrawable?.let {

        val bitmap = Bitmap.createBitmap(layerDrawable.intrinsicWidth, layerDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(bitmap)
        layerDrawable.setBounds(0, 0, canvas.width, canvas.height)
        layerDrawable.draw(canvas)

        val byteArrayOutputStream = ByteArrayOutputStream()

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)

        imageByteArray = byteArrayOutputStream.toByteArray()

    }

    return imageByteArray
}

fun bitmapToByteArray(bitmap: Bitmap?) : ByteArray? {

    var imageByteArray: ByteArray? = null

    bitmap?.let {

        val byteArrayOutputStream = ByteArrayOutputStream()

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)

        imageByteArray = byteArrayOutputStream.toByteArray()

    }

    return imageByteArray
}

fun getCircularBitmapWithWhiteBorder(
    bitmap: Bitmap?,
    borderWidth: Int,
    borderColor: Int
): Bitmap? {

    if (bitmap == null || bitmap.isRecycled) {

        return null
    }

    val width = bitmap.width
    val height = bitmap.height

    val canvasBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val bitmapShader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)

    val paint = Paint()

    paint.isAntiAlias = true
    paint.shader = bitmapShader

    val canvas = Canvas(canvasBitmap)
    val radius = if (width > height) height.toFloat() / 2f else width.toFloat() / 2f

    canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), radius, paint)

    paint.shader = null
    paint.style = Paint.Style.STROKE
    paint.color = borderColor
    paint.strokeWidth = borderWidth.toFloat()

    canvas.drawCircle((canvas.width / 2).toFloat(), (canvas.height / 2).toFloat(), radius - borderWidth / 2, paint)

    return canvasBitmap
}

fun rawStringToByteArray(rawString: String): ByteArray {

    val listOfRawString = rawString.replace("[", "").replace("]", "").split(",")

    val resultByteArray = ByteArray(listOfRawString.size)
    for (aByte in listOfRawString.withIndex()) {
        try {
            resultByteArray[aByte.index] = aByte.value.replace("\\s".toRegex(), "").toByte()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    return resultByteArray
}