/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 10/2/20 10:26 AM
 * Last modified 10/2/20 10:26 AM
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
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
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
import net.geeksempire.vicinity.android.Utils.UI.Display.DpToInteger
import net.geeksempire.vicinity.android.Utils.UI.Theme.ThemeType

class PublicCommunityAdapter(private val context: PublicCommunity,
                             firebaseRecyclerOptions: FirestoreRecyclerOptions<PublicMessageData>) : FirestoreRecyclerAdapter<PublicMessageData, RecyclerView.ViewHolder>(firebaseRecyclerOptions) {

    override fun getItemViewType(position: Int): Int {

        return (position)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (snapshots[viewType].userIdentifier == context.firebaseUser.uid) {
            PublicCommunitySelfViewHolder(LayoutInflater.from(context).inflate(R.layout.public_community_self_message_items, viewGroup, false))
        } else {
            PublicCommunityOthersViewHolder(LayoutInflater.from(context).inflate(R.layout.public_community_others_message_items, viewGroup, false))
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int, publicMessageData: PublicMessageData) {

        when (context.overallTheme.checkThemeLightDark()) {
            ThemeType.ThemeLight -> {


            }
            ThemeType.ThemeDark -> {


            }
        }

        if (snapshots[position].userIdentifier == context.firebaseUser.uid) {

            viewHolder as PublicCommunitySelfViewHolder

            viewHolder.userDisplayName.text = publicMessageData.userDisplayName
            viewHolder.userMessageTextContent.text = publicMessageData.userMessageTextContent

            publicMessageData.userMessageImageContent?.let { userMessageImageContent ->

                Glide.with(context)
                    .asDrawable()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .load(userMessageImageContent)
                    .transform(CenterCrop(), RoundedCorners(DpToInteger(context, 11)))
                    .into(viewHolder.userMessageImageContent)

                viewHolder.userMessageImageContent.visibility = View.VISIBLE

            }

            publicMessageData.userMessageDate?.let {

                viewHolder.userMessageDate.text = it.formatToCurrentTimeZone()

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

                                    viewHolder.userDisplayName.setTextColor(context.getColor(R.color.light))
                                    viewHolder.userMessageTextContent.setTextColor(context.getColor(R.color.light))
                                    viewHolder.userMessageDate.setTextColor(context.getColor(R.color.light_transparent))

                                } else {

                                    viewHolder.userDisplayName.setTextColor(context.getColor(R.color.dark))
                                    viewHolder.userMessageTextContent.setTextColor(context.getColor(R.color.dark))
                                    viewHolder.userMessageDate.setTextColor(context.getColor(R.color.dark_transparent))

                                }

                                viewHolder.messageContentWrapper.background = messageContentBackground

                                viewHolder.userProfileImage.setImageDrawable(it)
                            }

                        }

                        return false
                    }

                })
                .submit()

            viewHolder.rootViewItem.setOnClickListener {

                if (viewHolder.userMessageDate.isShown) {

                    viewHolder.userMessageDate.visibility = View.GONE

                } else {

                    viewHolder.userMessageDate.visibility = View.VISIBLE

                }

            }

            viewHolder.rootViewItem.setOnLongClickListener {



                false
            }

        } else {

            viewHolder as PublicCommunityOthersViewHolder

            viewHolder.userDisplayName.text = publicMessageData.userDisplayName
            viewHolder.userMessageTextContent.text = publicMessageData.userMessageTextContent

            publicMessageData.userMessageDate?.let {

                viewHolder.userMessageDate.text = it.formatToCurrentTimeZone()

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

                                    viewHolder.userDisplayName.setTextColor(context.getColor(R.color.light))
                                    viewHolder.userMessageTextContent.setTextColor(context.getColor(R.color.light))
                                    viewHolder.userMessageDate.setTextColor(context.getColor(R.color.light_transparent))

                                } else {

                                    viewHolder.userDisplayName.setTextColor(context.getColor(R.color.dark))
                                    viewHolder.userMessageTextContent.setTextColor(context.getColor(R.color.dark))
                                    viewHolder.userMessageDate.setTextColor(context.getColor(R.color.dark_transparent))

                                }

                                viewHolder.messageContentWrapper.background = messageContentBackground

                                viewHolder.userProfileImage.setImageDrawable(it)
                            }

                        }

                        return false
                    }

                })
                .submit()

            viewHolder.rootViewItem.setOnClickListener {

                if (viewHolder.userMessageDate.isShown) {

                    viewHolder.userMessageDate.visibility = View.GONE

                } else {

                    viewHolder.userMessageDate.visibility = View.VISIBLE

                }

            }

            viewHolder.rootViewItem.setOnLongClickListener {



                false
            }

        }

    }

    override fun onDataChanged() {

    }

    override fun onError(e: FirebaseFirestoreException) {
        super.onError(e)

        e.printStackTrace()

    }

}