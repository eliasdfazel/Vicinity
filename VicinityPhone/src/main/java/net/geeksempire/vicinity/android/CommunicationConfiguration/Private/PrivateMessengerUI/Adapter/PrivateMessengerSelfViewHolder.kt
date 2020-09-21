/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 9/21/20 10:29 AM
 * Last modified 9/21/20 10:11 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.CommunicationConfiguration.Private.PrivateMessengerUI.Adapter

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.private_messenger_self_message_items.view.*
import net.geekstools.imageview.customshapes.ShapesImage

class PrivateMessengerSelfViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    val rootViewItem: ConstraintLayout = view.rootViewItem
    val userProfileImage: ShapesImage = view.userProfileImage
    val messageContentWrapper: ConstraintLayout = view.messageContentWrapper
    val userDisplayName: TextView = view.userDisplayName
    val userMessageTextContent: TextView = view.userMessageTextContent
    val userMessageDate: TextView = view.userMessageDate
}