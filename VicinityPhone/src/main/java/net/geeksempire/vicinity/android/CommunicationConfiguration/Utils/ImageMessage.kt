/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 10/6/20 7:12 AM
 * Last modified 10/6/20 7:12 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.CommunicationConfiguration.Utils

import android.content.Intent
import android.graphics.drawable.Drawable
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import net.geeksempire.vicinity.android.R
import net.geeksempire.vicinity.android.databinding.PublicCommunityViewBinding

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

fun renderSelectedImagePreview(publicCommunityViewBinding: PublicCommunityViewBinding, listOfSelectedImages: ArrayList<Drawable>, imagePreview: Drawable) {

    if (listOfSelectedImages.size == 1) {

        publicCommunityViewBinding.imageMessageContentOne.setImageDrawable(imagePreview)
        publicCommunityViewBinding.imageMessageContentOne.visibility = View.VISIBLE

    } else if (listOfSelectedImages.size == 2) {

        publicCommunityViewBinding.imageMessageContentTwo.setImageDrawable(imagePreview)
        publicCommunityViewBinding.imageMessageContentTwo.visibility = View.VISIBLE

    } else if (listOfSelectedImages.size == 3) {

        publicCommunityViewBinding.imageMessageContentThree.setImageDrawable(imagePreview)
        publicCommunityViewBinding.imageMessageContentThree.visibility = View.VISIBLE

    } else {

        publicCommunityViewBinding.imageMessageContentOne.setImageDrawable(imagePreview)
        publicCommunityViewBinding.imageMessageContentOne.visibility = View.VISIBLE

    }

}