/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 10/2/20 6:54 AM
 * Last modified 10/2/20 6:53 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.CommunicationConfiguration.Utils

import android.content.Intent
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