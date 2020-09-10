/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 9/10/20 8:09 AM
 * Last modified 9/10/20 8:09 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.CommunicationConfiguration.Public.PublicCommunityUI.Adapter

import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestoreException
import net.geeksempire.vicinity.android.CommunicationConfiguration.Public.DataStructure.PublicMessageData
import net.geeksempire.vicinity.android.CommunicationConfiguration.Public.PublicCommunityUI.PublicCommunity
import net.geeksempire.vicinity.android.R
import net.geeksempire.vicinity.android.Utils.Calendar.formatToCurrentTimeZone
import net.geeksempire.vicinity.android.Utils.UI.Colors.extractDominantColor
import net.geeksempire.vicinity.android.Utils.UI.Colors.isColorDark
import net.geeksempire.vicinity.android.Utils.UI.Theme.ThemeType

class PublicCommunityAdapter(private val context: PublicCommunity,
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
            .listener(object : RequestListener<Drawable> {

                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {

                    return false
                }

                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {

                    context.runOnUiThread {

                        resource?.let {

                            val messageContentBackground: LayerDrawable = if (publicMessageData.userIdentifier == context.firebaseUser.uid) {
                                context.getDrawable(R.drawable.message_self_content_background) as LayerDrawable
                            } else {
                                context.getDrawable(R.drawable.message_others_content_background) as LayerDrawable
                            }

                            val dominantColor = extractDominantColor(context, it)

                            val temporaryBackground: Drawable = messageContentBackground.findDrawableByLayerId(R.id.temporaryBackground)
                            temporaryBackground.setTint(dominantColor)

                            if (isColorDark(dominantColor)) {

                                publicCommunityViewHolder.userDisplayName.setTextColor(context.getColor(R.color.light))
                                publicCommunityViewHolder.userMessageTextContent.setTextColor(context.getColor(R.color.light))
                                publicCommunityViewHolder.userMessageDate.setTextColor(context.getColor(R.color.light))

                            } else {

                                publicCommunityViewHolder.userDisplayName.setTextColor(context.getColor(R.color.dark))
                                publicCommunityViewHolder.userMessageTextContent.setTextColor(context.getColor(R.color.dark))
                                publicCommunityViewHolder.userMessageDate.setTextColor(context.getColor(R.color.dark))

                            }

                            publicCommunityViewHolder.messageContentWrapper.background = messageContentBackground

                            publicCommunityViewHolder.userProfileImage.setImageDrawable(it)
                        }

                    }

                    return false
                }

            })
            .submit()

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