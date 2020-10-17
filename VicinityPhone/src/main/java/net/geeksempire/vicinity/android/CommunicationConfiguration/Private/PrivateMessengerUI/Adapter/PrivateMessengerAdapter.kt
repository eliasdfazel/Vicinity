/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 10/17/20 6:37 AM
 * Last modified 10/17/20 6:37 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.CommunicationConfiguration.Private.PrivateMessengerUI.Adapter

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
import net.geeksempire.vicinity.android.CommunicationConfiguration.Private.DataStructure.PrivateMessageData
import net.geeksempire.vicinity.android.CommunicationConfiguration.Private.PrivateMessengerUI.PrivateMessenger
import net.geeksempire.vicinity.android.CommunicationConfiguration.Utils.ImageMessage.UI.MessageImagesViewer
import net.geeksempire.vicinity.android.R
import net.geeksempire.vicinity.android.Utils.Calendar.formatToCurrentTimeZone
import net.geeksempire.vicinity.android.Utils.UI.Colors.extractDominantColor
import net.geeksempire.vicinity.android.Utils.UI.Colors.isColorDark
import net.geeksempire.vicinity.android.Utils.UI.Display.DpToInteger
import net.geeksempire.vicinity.android.Utils.UI.Theme.ThemeType

class PrivateMessengerAdapter(private val context: PrivateMessenger,
                              firebaseRecyclerOptions: FirestoreRecyclerOptions<PrivateMessageData>) : FirestoreRecyclerAdapter<PrivateMessageData, RecyclerView.ViewHolder>(firebaseRecyclerOptions) {

    override fun getItemViewType(position: Int): Int {

        return (position)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (snapshots[viewType].userIdentifier == context.firebaseUser.uid) {
            PrivateMessengerSelfViewHolder(LayoutInflater.from(context).inflate(R.layout.public_community_self_message_items, viewGroup, false))
        } else {
            PrivateMessengerOthersViewHolder(LayoutInflater.from(context).inflate(R.layout.public_community_others_message_items, viewGroup, false))
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int, privateMessageData: PrivateMessageData) {

        when (context.overallTheme.checkThemeLightDark()) {
            ThemeType.ThemeLight -> {


            }
            ThemeType.ThemeDark -> {


            }
        }

        if (snapshots[position].userIdentifier == context.firebaseUser.uid) {

            viewHolder as PrivateMessengerSelfViewHolder

            viewHolder.userDisplayName.text = privateMessageData.userDisplayName
            viewHolder.userMessageTextContent.text = privateMessageData.userMessageTextContent

            privateMessageData.userMessageImageContent?.let { userMessageImageContent ->

                Glide.with(context)
                    .asDrawable()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .load(userMessageImageContent)
                    .transform(CenterCrop(), RoundedCorners(DpToInteger(context, 11)))
                    .into(viewHolder.userMessageImageContent)

                viewHolder.userMessageImageContent.visibility = View.VISIBLE

                viewHolder.userMessageImageContent.setOnClickListener {

                    val imagesStorageReference = privateMessageData.privateMessengerStorageImagesItemEndpoint

                    imagesStorageReference?.let {

                        context.privateMessengerViewBinding.fragmentContainer.visibility = View.VISIBLE
                        context.privateMessengerViewBinding.fragmentContainer.bringToFront()

                        context.messageImagesViewer.fragmentPlaceHolder = context.privateMessengerViewBinding.fragmentContainer

                        MessageImagesViewer.open(
                            activity = context,
                            messageImagesViewer = context.messageImagesViewer,
                            imageMessageDatabasePath = imagesStorageReference
                        )

                    }

                }

            }

            privateMessageData.userMessageDate?.let {

                viewHolder.userMessageDate.text = it.formatToCurrentTimeZone()

            }

            Glide.with(context)
                .asDrawable()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .load(privateMessageData.userProfileImage)
                .listener(object : RequestListener<Drawable> {

                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {

                        return false
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {

                        context.runOnUiThread {

                            resource?.let {

                                val messageContentBackground: LayerDrawable = if (privateMessageData.userIdentifier == context.firebaseUser.uid) {
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

            viewHolder as PrivateMessengerOthersViewHolder

            viewHolder.userDisplayName.text = privateMessageData.userDisplayName
            viewHolder.userMessageTextContent.text = privateMessageData.userMessageTextContent

            privateMessageData.userMessageImageContent?.let { userMessageImageContent ->

                Glide.with(context)
                    .asDrawable()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .load(userMessageImageContent)
                    .transform(CenterCrop(), RoundedCorners(DpToInteger(context, 11)))
                    .into(viewHolder.userMessageImageContent)

                viewHolder.userMessageImageContent.visibility = View.VISIBLE

                viewHolder.userMessageImageContent.setOnClickListener {

                    val imagesStorageReference = privateMessageData.privateMessengerStorageImagesItemEndpoint

                    imagesStorageReference?.let {

                        context.privateMessengerViewBinding.fragmentContainer.visibility = View.VISIBLE
                        context.privateMessengerViewBinding.fragmentContainer.bringToFront()

                        val messageImagesViewer = MessageImagesViewer()

                        messageImagesViewer.fragmentPlaceHolder = context.privateMessengerViewBinding.fragmentContainer

                        MessageImagesViewer.open(
                            activity = context,
                            messageImagesViewer = messageImagesViewer,
                            imageMessageDatabasePath = imagesStorageReference
                        )

                    }

                }

            }

            privateMessageData.userMessageDate?.let {

                viewHolder.userMessageDate.text = it.formatToCurrentTimeZone()

            }

            Glide.with(context)
                .asDrawable()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .load(privateMessageData.userProfileImage)
                .listener(object : RequestListener<Drawable> {

                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {

                        return false
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {

                        context.runOnUiThread {

                            resource?.let {

                                val messageContentBackground: LayerDrawable = if (privateMessageData.userIdentifier == context.firebaseUser.uid) {
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