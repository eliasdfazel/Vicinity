/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 10/23/20 6:03 AM
 * Last modified 10/23/20 5:48 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.CommunicationConfiguration.Utils.ImageMessage.UI.Adapter

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.annotation.Keep
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.message_images_item.view.*

@Keep
data class MessageImagesData (var imageMessage: Drawable)

class MessageImagesViewerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val rootView: ConstraintLayout = view.rootViewItem
    val imageMessageItem: ImageView = view.imageMessageItem
}