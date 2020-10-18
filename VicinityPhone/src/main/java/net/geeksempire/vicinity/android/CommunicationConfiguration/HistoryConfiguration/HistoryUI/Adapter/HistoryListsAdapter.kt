/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 10/18/20 10:15 AM
 * Last modified 10/18/20 10:15 AM
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
import com.google.android.gms.maps.model.LatLng
import net.geeksempire.vicinity.android.AccountManager.DataStructure.UserInformationVicinityArchiveData
import net.geeksempire.vicinity.android.CommunicationConfiguration.HistoryConfiguration.HistoryUI.HistoryLists
import net.geeksempire.vicinity.android.CommunicationConfiguration.Private.DataStructure.PrivateMessengerData
import net.geeksempire.vicinity.android.CommunicationConfiguration.Private.PrivateMessengerUI.PrivateMessenger
import net.geeksempire.vicinity.android.CommunicationConfiguration.Private.Utils.privateMessengerName
import net.geeksempire.vicinity.android.CommunicationConfiguration.Public.PublicCommunityUI.PublicCommunity
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

                historyListsViewHolder.communicationName.setTextColor(context.getColor(R.color.dark))

            }
            ThemeType.ThemeDark -> {

                historyListsViewHolder.communicationName.setTextColor(context.getColor(R.color.light))

            }
        }

        when (historyType) {
            HistoryType.PUBLIC -> {

                historyListsViewHolder.communicationName.text = publicMessengerData[position].vicinityKnownName

                historyListsViewHolder.communicationLogo.setImageDrawable(context.vicinityInformation.loadCountryFlag(publicMessengerData[position].vicinityCountry))

                historyListsViewHolder.rootViewItem.setOnClickListener {

                    PublicCommunity.open(
                        context = context,
                        currentCommunityCoordinates = LatLng(publicMessengerData[position].vicinityLatitude.toString().toDouble(), publicMessengerData[position].vicinityLongitude.toString().toDouble()),
                        nameOfCountry = publicMessengerData[position].vicinityCountry,
                    )

                }

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

                historyListsViewHolder.rootViewItem.setOnClickListener {

                    val selfUid = context.firebaseUser.uid
                    val selfUsername = context.firebaseUser.displayName
                    val selfProfileImage = context.firebaseUser.photoUrl.toString()

                    val otherUid = privateMessengerData[position].PersonOne
                    val otherUsername = privateMessengerData[position].PersonOneUsername
                    val otherProfileImage = privateMessengerData[position].PersonOneProfileImage

                    val privateMessengerName = privateMessengerName(selfUid, otherUid)

                    PrivateMessenger.open(
                        context = context,
                        privateMessengerName = privateMessengerName,
                        otherUid = otherUid,
                        otherUsername = otherUsername,
                        otherProfileImage = otherProfileImage
                    )

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