/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 12/7/20 7:41 AM
 * Last modified 12/7/20 7:12 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.MapConfiguration.Vicinity.People.UI.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import net.geeksempire.vicinity.android.AccountManager.DataStructure.PeopleData
import net.geeksempire.vicinity.android.R

class PeopleListAdapter (val context: Context) : RecyclerView.Adapter<PeopleListViewHolder>() {

    val peopleArrayList = ArrayList<PeopleData>()

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): PeopleListViewHolder {

        return PeopleListViewHolder(LayoutInflater.from(context).inflate(R.layout.people_list_item_view, viewGroup, false))
    }

    override fun getItemCount(): Int {

        return peopleArrayList.size
    }

    override fun onBindViewHolder(peopleListViewHolder: PeopleListViewHolder, position: Int) {

        println(">>>>>>>>>>>>>>>>>> " + position)

        peopleListViewHolder.userDisplayName.text = peopleArrayList[position].userDisplayName
        peopleListViewHolder.userEmailAddress.text = peopleArrayList[position].userEmailAddress

        Glide.with(context)
            .load(peopleArrayList[position].userProfileImage)
            .into(peopleListViewHolder.userProfileImage)

    }

}