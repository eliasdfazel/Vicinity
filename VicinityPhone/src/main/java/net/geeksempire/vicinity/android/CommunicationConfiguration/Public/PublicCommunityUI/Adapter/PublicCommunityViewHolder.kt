/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 9/8/20 10:55 AM
 * Last modified 9/8/20 10:55 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.CommunicationConfiguration.Public.PublicCommunityUI.Adapter

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.public_community_self_message_items.view.*

class PublicCommunityViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    val rootViewItem: ConstraintLayout = view.rootViewItem
//    val userProfileImage: ShapesImage = view.userProfileImage
//    val userDisplayName: TextView = view.userDisplayName
    val userMessageTextContent: TextView = view.userMessageTextContent
//    val userMessageDate: TextView = view.userMessageDate
}