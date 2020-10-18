/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 10/18/20 9:14 AM
 * Last modified 10/18/20 9:09 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.CommunicationConfiguration.HistoryConfiguration.HistoryUI.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import net.geeksempire.vicinity.android.AccountManager.DataStructure.UserInformationVicinityArchiveData
import net.geeksempire.vicinity.android.CommunicationConfiguration.HistoryConfiguration.HistoryUI.HistoryLists
import net.geeksempire.vicinity.android.CommunicationConfiguration.Private.DataStructure.PrivateMessengerData
import net.geeksempire.vicinity.android.R
import net.geeksempire.vicinity.android.Utils.UI.Theme.ThemeType

object HistoryType {
    const val PUBLIC = "public"
    const val PRIVATE = "private"
}

class HistoryListsAdapter(private val context: HistoryLists) : RecyclerView.Adapter<HistoryListsViewHolder>() {

    var historyType: String = "public"

    val publicMessengerData: ArrayList<UserInformationVicinityArchiveData> = ArrayList<UserInformationVicinityArchiveData>()
    val privateMessengerData: ArrayList<PrivateMessengerData> = ArrayList<PrivateMessengerData>()

    override fun getItemViewType(position: Int): Int {

        return (position)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): HistoryListsViewHolder {

        return HistoryListsViewHolder(LayoutInflater.from(context).inflate(R.layout.history_lists_items, viewGroup, false))
    }

    override fun onBindViewHolder(historyListsViewHolder: HistoryListsViewHolder, position: Int) {

        when (context.overallTheme.checkThemeLightDark()) {
            ThemeType.ThemeLight -> {


            }
            ThemeType.ThemeDark -> {


            }
        }

        when (historyType) {
            HistoryType.PUBLIC -> {

                historyListsViewHolder.communicationName.text = publicMessengerData[position].vicinityKnownName

            }
            HistoryType.PRIVATE -> {

                historyListsViewHolder.communicationName.text = "${privateMessengerData[position].PersonOneUsername}" +
                        " with " +
                        "${privateMessengerData[position].PersonTwoUsername}"

            }
        }

    }

    override fun getItemCount(): Int {

        return when (historyType) {
            HistoryType.PUBLIC -> {

                publicMessengerData.size

            }
            HistoryType.PRIVATE -> {

                privateMessengerData.size

            }
            else -> 0
        }
    }

}