/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 10/5/20 6:16 AM
 * Last modified 10/5/20 5:06 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.CommunicationConfiguration.Utils

import android.content.Intent
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import net.geeksempire.vicinity.android.R

const val IMAGE_PICKER_REQUEST_CODE: Int = 123
const val IMAGE_CAPTURE_REQUEST_CODE: Int = 456

fun startImagePicker(context: AppCompatActivity) {

    val imagePicker = Intent(Intent.ACTION_GET_CONTENT)
    imagePicker.type = "image/*"
    context.startActivityForResult(Intent.createChooser(imagePicker, context.getString(R.string.shareImage)), IMAGE_PICKER_REQUEST_CODE)

}

fun startImageCapture(context: AppCompatActivity) {

    val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    context.startActivityForResult(takePictureIntent, IMAGE_CAPTURE_REQUEST_CODE)

}

fun renderSelectedImagePreview(selectedImagePreview: LayerDrawable, imageIndex: Int, selectedImage: Drawable) : Drawable {

    selectedImagePreview.setDrawable(imageIndex, selectedImage)

    return selectedImagePreview
}