/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 10/18/20 9:52 AM
 * Last modified 10/18/20 9:51 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.CommunicationConfiguration.HistoryConfiguration.HistoryUI.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
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

                historyListsViewHolder.communicationLogo.setImageDrawable(context.vicinityInformation.loadCountryFlag("United States"/*publicMessengerData[position].vicinityCountry*/))

            }
            HistoryType.PRIVATE -> {

                if (privateMessengerData[position].PersonOne == context.firebaseUser.uid) {

                    historyListsViewHolder.communicationName.text = "${privateMessengerData[position].PersonTwoUsername}" +
                            " with " +
                            "${privateMessengerData[position].PersonOneUsername}"

                    Glide.with(context)
                        .load(privateMessengerData[position].PersonTwoProfileImage)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(historyListsViewHolder.communicationLogo)

                } else {

                    historyListsViewHolder.communicationName.text = "${privateMessengerData[position].PersonOneUsername}" +
                            " with " +
                            "${privateMessengerData[position].PersonTwoUsername}"

                    Glide.with(context)
                        .load(privateMessengerData[position].PersonOneProfileImage)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(historyListsViewHolder.communicationLogo)

                }

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