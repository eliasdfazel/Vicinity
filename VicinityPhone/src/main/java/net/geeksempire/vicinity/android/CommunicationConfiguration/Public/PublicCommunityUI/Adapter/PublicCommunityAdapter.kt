/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 9/9/20 8:34 AM
 * Last modified 9/9/20 8:34 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.CommunicationConfiguration.Public.PublicCommunityUI.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestoreException
import net.geeksempire.vicinity.android.CommunicationConfiguration.Public.DataStructure.PublicMessageData
import net.geeksempire.vicinity.android.CommunicationConfiguration.Public.PublicCommunityUI.PublicCommunity
import net.geeksempire.vicinity.android.R
import net.geeksempire.vicinity.android.Utils.Calendar.formatToCurrentTimeZone
import net.geeksempire.vicinity.android.Utils.UI.Theme.ThemeType

class PublicCommunityAdapter(private val context: PublicCommunity,
    firebaseRecyclerOptions: FirestoreRecyclerOptions<PublicMessageData>) : FirestoreRecyclerAdapter<PublicMessageData, PublicCommunityViewHolder>(firebaseRecyclerOptions) {

    override fun getItemViewType(position: Int): Int {

        return (position)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): PublicCommunityViewHolder {

        return if (snapshots[viewType].userIdentifier == context.firebaseUser.uid) {
            PublicCommunityViewHolder(
                LayoutInflater.from(context).inflate(
                    R.layout.public_community_self_message_items,
                    viewGroup,
                    false
                )
            )
        } else {
            PublicCommunityViewHolder(
                LayoutInflater.from(context).inflate(
                    R.layout.public_community_others_message_items,
                    viewGroup,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(
        publicCommunityViewHolder: PublicCommunityViewHolder,
        position: Int,
        publicMessageData: PublicMessageData
    ) {

        when (context.overallTheme.checkThemeLightDark()) {
            ThemeType.ThemeLight -> {


            }
            ThemeType.ThemeDark -> {


            }
        }

        publicCommunityViewHolder.userDisplayName.text = publicMessageData.userDisplayName
        publicCommunityViewHolder.userMessageTextContent.text = publicMessageData.userMessageTextContent

        publicMessageData.userMessageDate?.let {

            publicCommunityViewHolder.userMessageDate.text = it.formatToCurrentTimeZone()

        }

        Glide.with(context)
            .asDrawable()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .load(publicMessageData.userProfileImage)
            .into(publicCommunityViewHolder.userProfileImage)

        publicCommunityViewHolder.rootViewItem.setOnClickListener {

            if (publicCommunityViewHolder.userMessageDate.isShown) {

                publicCommunityViewHolder.userMessageDate.visibility = View.GONE

            } else {

                publicCommunityViewHolder.userMessageDate.visibility = View.VISIBLE

            }

        }

        publicCommunityViewHolder.rootViewItem.setOnLongClickListener {



            false
        }

    }

    override fun onDataChanged() {

    }

    override fun onError(e: FirebaseFirestoreException) {
        super.onError(e)

        e.printStackTrace()

    }

}