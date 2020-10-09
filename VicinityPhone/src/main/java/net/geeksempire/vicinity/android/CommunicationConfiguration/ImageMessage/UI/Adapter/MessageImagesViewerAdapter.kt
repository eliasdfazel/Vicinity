/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 10/9/20 7:19 AM
 * Last modified 10/9/20 7:08 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.CommunicationConfiguration.ImageMessage.UI.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import net.geeksempire.vicinity.android.R

class MessageImagesViewerAdapter (private val context: Context) : RecyclerView.Adapter<MessageImagesViewerViewHolder>() {

    val messageImagesData: ArrayList<MessageImagesData> = ArrayList<MessageImagesData>()

    override fun getItemCount(): Int {

        return messageImagesData.size
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MessageImagesViewerViewHolder {

        return MessageImagesViewerViewHolder(LayoutInflater.from(context).inflate(R.layout.message_images_item, viewGroup, false))
    }

    override fun onBindViewHolder(messageImagesViewerViewHolder: MessageImagesViewerViewHolder, position: Int) {

        Glide.with(context)
            .load(messageImagesData[position].imageMessage)
            .into(messageImagesViewerViewHolder.imageMessageItem)

        messageImagesViewerViewHolder.imageMessageItem.setOnClickListener {



        }

    }

}