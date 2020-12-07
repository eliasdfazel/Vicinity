/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 12/7/20 7:41 AM
 * Last modified 12/7/20 7:41 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.MapConfiguration.Vicinity.People.UI.Adapter

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.people_list_item_view.view.*
import net.geekstools.imageview.customshapes.ShapesImage

class PeopleListViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    val rootViewItem: ConstraintLayout = view.rootViewItem
    val userProfileImage: ShapesImage = view.userProfileImage
    val userDisplayName: TextView = view.userDisplayName
    val userEmailAddress: TextView = view.userEmailAddress
}