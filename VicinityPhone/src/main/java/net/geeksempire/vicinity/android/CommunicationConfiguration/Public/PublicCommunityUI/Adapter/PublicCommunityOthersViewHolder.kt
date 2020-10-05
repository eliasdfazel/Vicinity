/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 10/5/20 8:58 AM
 * Last modified 10/5/20 8:58 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.CommunicationConfiguration.Public.PublicCommunityUI.Adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.public_community_others_message_items.view.*
import net.geekstools.imageview.customshapes.ShapesImage

class PublicCommunityOthersViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    val rootViewItem: ConstraintLayout = view.rootViewItem
    val userProfileImage: ShapesImage = view.userProfileImage
    val userMessageImageContent: ImageView = view.userMessageImageContent
    val messageContentWrapper: ConstraintLayout = view.messageContentWrapper
    val userDisplayName: TextView = view.userDisplayName
    val userMessageTextContent: TextView = view.userMessageTextContent
    val userMessageDate: TextView = view.userMessageDate
}