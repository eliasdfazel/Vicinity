/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 10/9/20 7:19 AM
 * Last modified 10/9/20 7:09 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.CommunicationConfiguration.ImageMessage.UI

import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import net.geeksempire.vicinity.android.CommunicationConfiguration.ImageMessage.UI.Adapter.MessageImagesData
import net.geeksempire.vicinity.android.CommunicationConfiguration.ImageMessage.UI.Adapter.MessageImagesViewerAdapter
import net.geeksempire.vicinity.android.CommunicationConfiguration.ImageMessage.Utils.IMAGE_MESSAGE_DATA_STRUCTURE
import net.geeksempire.vicinity.android.R
import net.geeksempire.vicinity.android.databinding.MessageImagesViewBinding


class MessageImagesViewer : Fragment() {

    companion object {

        fun open(
            activity: AppCompatActivity,
            messageImagesViewer: MessageImagesViewer,
            imageMessageDatabasePath: String
        ) {

            messageImagesViewer.arguments = Bundle().apply {
                putString(IMAGE_MESSAGE_DATA_STRUCTURE, imageMessageDatabasePath)
            }

            activity.supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, 0)
                .replace(R.id.fragmentContainer, messageImagesViewer, "Image Viewer")
                .commit()

        }

    }

    var imageMessageDatabasePath: String? = null

    var fragmentPlaceHolder: FrameLayout? = null

    val messageImagesViewerAdapter: MessageImagesViewerAdapter by lazy {
        MessageImagesViewerAdapter(requireContext())
    }

    lateinit var messageImagesViewBinding: MessageImagesViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        imageMessageDatabasePath = arguments?.getString(IMAGE_MESSAGE_DATA_STRUCTURE)

    }

    override fun onCreateView(layoutInflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(layoutInflater, container, savedInstanceState)

        messageImagesViewBinding = MessageImagesViewBinding.inflate(layoutInflater)

        return messageImagesViewBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (imageMessageDatabasePath != null) {

            messageImagesViewBinding.imagesRecyclerView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

            messageImagesViewBinding.imagesRecyclerView.adapter = messageImagesViewerAdapter

            for (imageIndex in 0..2) {

                val imageMessageStorage = imageMessageDatabasePath.plus("/Image${imageIndex}.JPEG")

                Firebase.storage.reference
                    .child(imageMessageStorage)
                    .downloadUrl.addOnSuccessListener { downloadLink ->

                        downloadImage(downloadLink)

                    }.addOnFailureListener {



                    }

            }

        } else {

            requireActivity().supportFragmentManager.beginTransaction().remove(this@MessageImagesViewer)

        }

        this@MessageImagesViewer.view?.setOnKeyListener { view, keyCode, keyEvent ->

            when (keyCode) {
                KeyEvent.KEYCODE_BACK -> {

                    requireActivity().supportFragmentManager.beginTransaction().remove(this@MessageImagesViewer)

                }
            }

            true
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()

        fragmentPlaceHolder?.visibility = View.GONE

    }

    private fun downloadImage(downloadLink: Uri) {

        Glide.with(requireContext())
            .load(downloadLink)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {

                    return false
                }

                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {

                    resource?.let {

                        requireActivity().runOnUiThread {

                            messageImagesViewerAdapter.messageImagesData.add(
                                MessageImagesData(
                                    imageMessage = resource
                                )
                            )

                            messageImagesViewerAdapter.notifyDataSetChanged()

                        }

                    }

                    return false
                }

            })
            .submit()

    }

}