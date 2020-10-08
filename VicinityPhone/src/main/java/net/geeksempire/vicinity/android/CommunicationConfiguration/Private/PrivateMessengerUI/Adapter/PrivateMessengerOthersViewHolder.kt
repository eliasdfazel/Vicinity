/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 10/8/20 7:59 AM
 * Last modified 10/8/20 7:43 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.CommunicationConfiguration.Private.PrivateMessengerUI.Adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.private_messenger_others_message_items.view.*
import net.geekstools.imageview.customshapes.ShapesImage

class PrivateMessengerOthersViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    val rootViewItem: ConstraintLayout = view.rootViewItem
    val userProfileImage: ShapesImage = view.userProfileImage
    val userMessageImageContent: ImageView = view.userMessageImageContent
    val messageContentWrapper: ConstraintLayout = view.messageContentWrapper
    val userDisplayName: TextView = view.userDisplayName
    val userMessageTextContent: TextView = view.userMessageTextContent
    val userMessageDate: TextView = view.userMessageDate
}