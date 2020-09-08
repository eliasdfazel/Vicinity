/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 9/8/20 10:55 AM
 * Last modified 9/8/20 10:33 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.CommunicationConfiguration.Public.PublicCommunityUI.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestoreException
import net.geeksempire.vicinity.android.CommunicationConfiguration.Public.DataStructure.PublicMessageData
import net.geeksempire.vicinity.android.CommunicationConfiguration.Public.PublicCommunityUI.PublicCommunity
import net.geeksempire.vicinity.android.R

class PublicCommunityAdapter (private val context: PublicCommunity,
                              firebaseRecyclerOptions: FirestoreRecyclerOptions<PublicMessageData>) : FirestoreRecyclerAdapter<PublicMessageData, PublicCommunityViewHolder>(firebaseRecyclerOptions) {

    override fun getItemViewType(position: Int): Int {

        return (position)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): PublicCommunityViewHolder {

        return if (snapshots[viewType].userIdentifier == context.firebaseUser.uid) {
            PublicCommunityViewHolder(LayoutInflater.from(context).inflate(R.layout.public_community_self_message_items, viewGroup, false))
        } else {
            PublicCommunityViewHolder(LayoutInflater.from(context).inflate(R.layout.public_community_others_message_items, viewGroup, false))
        }
    }

    override fun onBindViewHolder(publicCommunityViewHolder: PublicCommunityViewHolder, position: Int, publicMessageData: PublicMessageData) {

        publicCommunityViewHolder.userMessageTextContent.text = publicMessageData.userMessageTextContent

    }

    override fun onDataChanged() {

    }

    override fun onError(e: FirebaseFirestoreException) {
        super.onError(e)
        e.printStackTrace()
    }

}