/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 10/17/20 6:37 AM
 * Last modified 10/17/20 4:47 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.CommunicationConfiguration.Utils.ImageMessage.UI.Adapter

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.message_images_item.view.*

data class MessageImagesData (var imageMessage: Drawable)

class MessageImagesViewerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val rootView: ConstraintLayout = view.rootViewItem
    val imageMessageItem: ImageView = view.imageMessageItem
}