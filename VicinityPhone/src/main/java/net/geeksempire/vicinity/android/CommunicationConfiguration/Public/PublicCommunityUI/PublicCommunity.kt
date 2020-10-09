/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 10/9/20 7:19 AM
 * Last modified 10/9/20 5:27 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.CommunicationConfiguration.Public.PublicCommunityUI

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.ktx.storage
import net.geeksempire.vicinity.android.CommunicationConfiguration.ImageMessage.Utils.*
import net.geeksempire.vicinity.android.CommunicationConfiguration.Public.DataStructure.PublicMessageData
import net.geeksempire.vicinity.android.CommunicationConfiguration.Public.Endpoint.PublicCommunicationEndpoint
import net.geeksempire.vicinity.android.CommunicationConfiguration.Public.Extensions.publicCommunityPrepareMessage
import net.geeksempire.vicinity.android.CommunicationConfiguration.Public.Extensions.publicCommunityPrepareNotificationData
import net.geeksempire.vicinity.android.CommunicationConfiguration.Public.Extensions.publicCommunityPrepareNotificationTopic
import net.geeksempire.vicinity.android.CommunicationConfiguration.Public.Extensions.publicCommunitySetupUI
import net.geeksempire.vicinity.android.CommunicationConfiguration.Public.PublicCommunityUI.Adapter.PublicCommunityAdapter
import net.geeksempire.vicinity.android.MapConfiguration.Vicinity.vicinityName
import net.geeksempire.vicinity.android.R
import net.geeksempire.vicinity.android.Utils.Networking.NetworkCheckpoint
import net.geeksempire.vicinity.android.Utils.Networking.NetworkConnectionListener
import net.geeksempire.vicinity.android.Utils.Networking.NetworkConnectionListenerInterface
import net.geeksempire.vicinity.android.Utils.UI.Display.DpToInteger
import net.geeksempire.vicinity.android.Utils.UI.Images.bitmapToByteArray
import net.geeksempire.vicinity.android.Utils.UI.Images.drawableToByteArray
import net.geeksempire.vicinity.android.Utils.UI.Images.takeViewSnapshot
import net.geeksempire.vicinity.android.Utils.UI.Theme.OverallTheme
import net.geeksempire.vicinity.android.VicinityApplication
import net.geeksempire.vicinity.android.databinding.PublicCommunityViewBinding
import javax.inject.Inject
import kotlin.math.roundToInt

class PublicCommunity : AppCompatActivity(), NetworkConnectionListenerInterface {

    object Configurations {
        const val PublicCommunityName: String = "PublicCommunityName"
        const val PublicCommunityDatabasePath: String = "PublicCommunityDatabasePath"

        const val PublicCommunityCountryName: String = "CountryName"
        const val PublicCommunityCenterLocationLatitude: String = "VicinityLatitude"
        const val PublicCommunityCenterLocationLongitude: String = "VicinityLongitude"

        const val NotificationCloudFunction: String = "publicCommunityNewMessageNotification"
    }

    companion object {

        fun open(context: Context, currentCommunityCoordinates: LatLng, nameOfCountry: String) {
            context.startActivity(
                Intent(context, PublicCommunity::class.java).apply {
                    putExtra(
                        PublicCommunity.Configurations.PublicCommunityName, vicinityName(
                            currentCommunityCoordinates
                        )
                    )
                    putExtra(
                        PublicCommunity.Configurations.PublicCommunityDatabasePath,
                        PublicCommunicationEndpoint.publicCommunityDocumentEndpoint(
                            nameOfCountry,
                            currentCommunityCoordinates
                        )
                    )
                    putExtra(
                        PublicCommunity.Configurations.PublicCommunityCountryName,
                        nameOfCountry
                    )
                    putExtra(
                        PublicCommunity.Configurations.PublicCommunityCenterLocationLatitude,
                        currentCommunityCoordinates.latitude
                    )
                    putExtra(
                        PublicCommunity.Configurations.PublicCommunityCenterLocationLongitude,
                        currentCommunityCoordinates.longitude
                    )
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                },
                ActivityOptions.makeCustomAnimation(context, R.anim.slide_in_right, R.anim.fade_out)
                    .toBundle()
            )
        }

    }

    val overallTheme: OverallTheme by lazy {
        OverallTheme(applicationContext)
    }

    val firestoreDatabase: FirebaseFirestore = Firebase.firestore

    val firebaseUser: FirebaseUser = Firebase.auth.currentUser!!

    private lateinit var firebaseRecyclerAdapter: FirestoreRecyclerAdapter<PublicMessageData, RecyclerView.ViewHolder>

    val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(this@PublicCommunity, RecyclerView.VERTICAL, false)
    }

    private val firebaseCloudFunctions: FirebaseFunctions = FirebaseFunctions.getInstance()

    val listOfSelectedImages: ArrayList<Drawable> = ArrayList<Drawable>(3)

    @Inject lateinit var networkCheckpoint: NetworkCheckpoint

    @Inject lateinit var networkConnectionListener: NetworkConnectionListener

    lateinit var publicCommunityViewBinding: PublicCommunityViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        publicCommunityViewBinding = PublicCommunityViewBinding.inflate(layoutInflater)
        setContentView(publicCommunityViewBinding.root)

        try {
            val firebaseFirestoreSettings = firestoreSettings {
                isPersistenceEnabled = false
                cacheSizeBytes = FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED
            }

            firestoreDatabase.firestoreSettings = firebaseFirestoreSettings
        } catch (e: Exception) {
            e.printStackTrace()
        }

        (application as VicinityApplication)
            .dependencyGraph
            .subDependencyGraph()
            .create(this@PublicCommunity, publicCommunityViewBinding.rootView)
            .inject(this@PublicCommunity)

        networkConnectionListener.networkConnectionListenerInterface = this@PublicCommunity

        val publicCommunityName: String? = intent.getStringExtra(PublicCommunity.Configurations.PublicCommunityName)
        val publicCommunityMessagesDatabasePath: String = intent.getStringExtra(PublicCommunity.Configurations.PublicCommunityDatabasePath).plus(
            "/Messages"
        )

        val publicCommunityCountryName: String? = intent.getStringExtra(PublicCommunity.Configurations.PublicCommunityCountryName)

        val communityCenterVicinity = LatLng(
            intent.getDoubleExtra(
                PublicCommunity.Configurations.PublicCommunityCenterLocationLatitude,
                0.0
            ),
            intent.getDoubleExtra(
                PublicCommunity.Configurations.PublicCommunityCenterLocationLongitude,
                0.0
            )
        )


        publicCommunityName?.let {

            FirebaseMessaging.getInstance().subscribeToTopic(
                publicCommunityPrepareNotificationTopic(
                    publicCommunityName
                )
            )
                .addOnSuccessListener {
                    Log.d(
                        this@PublicCommunity.javaClass.simpleName,
                        "Subscribed To ${publicCommunityName}"
                    )

                }.addOnFailureListener {
                    Log.d(this@PublicCommunity.javaClass.simpleName, "Failed To Subscribe")

                }

        }

        publicCommunitySetupUI()

        linearLayoutManager.stackFromEnd = false

        val publicMessageCollectionReference: Query = firestoreDatabase
            .collection(publicCommunityMessagesDatabasePath)
            .orderBy("userMessageDate", Query.Direction.ASCENDING)

        val firebaseRecyclerOptions = FirestoreRecyclerOptions.Builder<PublicMessageData>()
            .setQuery(publicMessageCollectionReference, PublicMessageData::class.java)
            .build()

        firebaseRecyclerAdapter = PublicCommunityAdapter(
            this@PublicCommunity,
            firebaseRecyclerOptions
        )

        publicCommunityViewBinding.messageRecyclerView.layoutManager = linearLayoutManager
        publicCommunityViewBinding.messageRecyclerView.adapter = firebaseRecyclerAdapter

        firebaseRecyclerAdapter.registerAdapterDataObserver(object :
            RecyclerView.AdapterDataObserver() {

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)

                val friendlyMessageCount = firebaseRecyclerAdapter.itemCount
                val lastVisiblePosition =
                    linearLayoutManager.findLastCompletelyVisibleItemPosition()

                if (lastVisiblePosition == -1 || positionStart >= friendlyMessageCount - 1 && lastVisiblePosition == positionStart - 1) {
                    Handler(Looper.getMainLooper()).postDelayed({

                        publicCommunityViewBinding.nestedScrollView.smoothScrollTo(
                            0,
                            publicCommunityViewBinding.messageRecyclerView.height
                        )

                        publicCommunityViewBinding.loadingView.visibility = View.GONE

                    }, 500)
                }

            }

        })

        publicCommunityViewBinding.sendMessageView.setOnClickListener {

            if (publicCommunityViewBinding.textMessageContentView.text.toString().isNotEmpty()
                || publicCommunityViewBinding.imageMessageContentView.isShown) {

                val publicCommunityPrepareMessage = publicCommunityPrepareMessage()
                firestoreDatabase
                    .collection(publicCommunityMessagesDatabasePath)
                    .add(publicCommunityPrepareMessage)
                    .addOnSuccessListener { documentSnapshot ->

                        if (publicCommunityViewBinding.imageMessageContentView.isShown) {

                            repeat(listOfSelectedImages.size) { imageIndex ->

                                val sentMessagePathForImages = PublicCommunicationEndpoint.publicCommunityStorageImagesItemEndpoint(
                                    publicCommunityMessagesDatabasePath = publicCommunityMessagesDatabasePath,
                                    documentSnapshotId = documentSnapshot.id,
                                    imageIndex = imageIndex.toString()
                                )

                                drawableToByteArray(listOfSelectedImages[imageIndex])?.let { drawableByteArray ->

                                    val firebaseStorage = Firebase.storage
                                    val storageReference = firebaseStorage.reference
                                        .child(sentMessagePathForImages)
                                    storageReference
                                        .putBytes(drawableByteArray)
                                        .addOnSuccessListener {

                                        }

                                }

                            }

                            listOfSelectedImages.clear()

                            val sentMessagePath = publicCommunityMessagesDatabasePath + "/" + documentSnapshot.id

                            val sentMessagePathForImages = PublicCommunicationEndpoint.publicCommunityStoragePreviewImageEndpoint(
                                publicCommunityMessagesDatabasePath = publicCommunityMessagesDatabasePath,
                                documentSnapshotId = documentSnapshot.id)

                            bitmapToByteArray(takeViewSnapshot(publicCommunityViewBinding.imageMessageContentView))?.let { drawableByteArray ->

                                val firebaseStorage = Firebase.storage
                                val storageReference = firebaseStorage.reference
                                    .child(sentMessagePathForImages)
                                storageReference
                                    .putBytes(drawableByteArray)
                                    .addOnSuccessListener {

                                        storageReference.downloadUrl.addOnSuccessListener { imageDownloadLink ->

                                            firestoreDatabase
                                                .document(sentMessagePath)
                                                .update(
                                                    "userMessageImageContent", imageDownloadLink.toString(),
                                                    "publicCommunityStorageImagesItemEndpoint", PublicCommunicationEndpoint.publicCommunityStorageImagesItemEndpoint(publicCommunityMessagesDatabasePath, documentSnapshot.id),
                                                )

                                        }

                                    }

                            }

                        }

                        publicCommunityViewBinding.sendMessageView.setAnimation(R.raw.sending_animation)
                        publicCommunityViewBinding.sendMessageView.playAnimation()
                        publicCommunityViewBinding.sendMessageView.addAnimatorUpdateListener { valueAnimator ->

                            val animationProgress = (valueAnimator.animatedValue as Float * 100).roundToInt()

                            if (animationProgress == 96) {

                                Handler(Looper.getMainLooper()).postDelayed({

                                    val animationSpeed =
                                        publicCommunityViewBinding.sendMessageView.speed

                                    publicCommunityViewBinding.sendMessageView.speed = -(animationSpeed)
                                    publicCommunityViewBinding.sendMessageView.playAnimation()

                                }, 157)

                            }

                        }

                        val messageContent = publicCommunityViewBinding.textMessageContentView.text.toString()

                        if (publicCommunityName != null && publicCommunityCountryName != null) {

                            firebaseCloudFunctions
                                .getHttpsCallable(PublicCommunity.Configurations.NotificationCloudFunction)
                                .call(publicCommunityPrepareNotificationData(
                                    messageContent,
                                    publicCommunityName,
                                    publicCommunityCountryName,
                                    communityCenterVicinity
                                ))
                                .continueWith {

                                }

                        }

                        publicCommunityViewBinding.textMessageContentView.text = null
                        publicCommunityViewBinding.imageMessageContentOne.setImageDrawable(null)
                        publicCommunityViewBinding.imageMessageContentTwo.setImageDrawable(null)
                        publicCommunityViewBinding.imageMessageContentThree.setImageDrawable(null)
                        publicCommunityViewBinding.imageMessageContentView.visibility = View.GONE

                        publicCommunityViewBinding.nestedScrollView.smoothScrollTo(0, publicCommunityViewBinding.messageRecyclerView.height)

                    }.addOnFailureListener {

                        publicCommunityViewBinding.textMessageContentLayout.error = getString(R.string.messageSentError)
                        publicCommunityViewBinding.textMessageContentLayout.isErrorEnabled = true

                        publicCommunityViewBinding.sendMessageView.setAnimation(R.raw.sending_animation_error)
                        publicCommunityViewBinding.sendMessageView.playAnimation()

                        publicCommunityViewBinding.sendMessageView.addAnimatorUpdateListener { valueAnimator ->

                            val animationProgress = (valueAnimator.animatedValue as Float * 100).roundToInt()

                            if (animationProgress == 96) {

                                publicCommunityViewBinding.sendMessageView.setAnimation(R.raw.sending_animation)

                            }

                        }

                    }

            }

        }

        publicCommunityViewBinding.addImageView.setOnClickListener {

            publicCommunityViewBinding.addImageView.isEnabled = false

            if (publicCommunityViewBinding.imageGallery.isShown && publicCommunityViewBinding.imageCapture.isShown) {

                publicCommunityViewBinding.imageGallery.startAnimation(TranslateAnimation(
                    0f,
                    -(DpToInteger(applicationContext, 51)).toFloat(),
                    0f,
                    0f
                ).apply {
                    duration = 379
                })

                publicCommunityViewBinding.imageCapture.startAnimation(TranslateAnimation(
                    0f,
                    -(DpToInteger(applicationContext, 101)).toFloat(),
                    0f,
                    0f
                ).apply {
                    duration = 379
                })

                Handler(Looper.getMainLooper()).postDelayed({

                    publicCommunityViewBinding.imageGallery.visibility = View.INVISIBLE
                    publicCommunityViewBinding.imageGallery.startAnimation(AnimationUtils.loadAnimation(applicationContext, android.R.anim.fade_out))

                    publicCommunityViewBinding.imageCapture.visibility = View.INVISIBLE
                    publicCommunityViewBinding.imageCapture.startAnimation(AnimationUtils.loadAnimation(applicationContext, android.R.anim.fade_out))

                    publicCommunityViewBinding.addImageView.apply {
                        setMinAndMaxFrame(41, 75)
                    }.playAnimation()

                }, 379)

            } else {

                publicCommunityViewBinding.addImageView.apply {
                    setMinAndMaxFrame(0, 41)
                }.playAnimation()
                publicCommunityViewBinding.addImageView.addAnimatorUpdateListener { valueAnimator ->

                    val animationProgress = (valueAnimator.animatedValue as Float * 100).roundToInt()

                    if (animationProgress == 49) {

                        publicCommunityViewBinding.imageGallery.visibility = View.VISIBLE
                        publicCommunityViewBinding.imageGallery.startAnimation(TranslateAnimation(
                            -(DpToInteger(applicationContext, 51)).toFloat(),
                            0f,
                            0f,
                            0f
                        ).apply {
                            duration = 555
                        })

                        publicCommunityViewBinding.imageCapture.visibility = View.VISIBLE
                        publicCommunityViewBinding.imageCapture.startAnimation(TranslateAnimation(
                            -(DpToInteger(applicationContext, 101)).toFloat(),
                            0f,
                            0f,
                            0f
                        ).apply {
                            duration = 555
                        })

                    }

                }

            }

            Handler(Looper.getMainLooper()).postDelayed({
                publicCommunityViewBinding.addImageView.isEnabled = true
            }, 1531)

        }

        publicCommunityViewBinding.imageGallery.setOnClickListener {

            publicCommunityViewBinding.imageGallery.visibility = View.INVISIBLE
            publicCommunityViewBinding.imageGallery.startAnimation(AnimationUtils.loadAnimation(applicationContext, android.R.anim.fade_out))

            publicCommunityViewBinding.imageCapture.visibility = View.INVISIBLE
            publicCommunityViewBinding.imageCapture.startAnimation(AnimationUtils.loadAnimation(applicationContext, android.R.anim.fade_out))

            startImagePicker(this@PublicCommunity)

        }

        publicCommunityViewBinding.imageCapture.setOnClickListener {

            publicCommunityViewBinding.imageGallery.visibility = View.INVISIBLE
            publicCommunityViewBinding.imageGallery.startAnimation(AnimationUtils.loadAnimation(applicationContext, android.R.anim.fade_out))

            publicCommunityViewBinding.imageCapture.visibility = View.INVISIBLE
            publicCommunityViewBinding.imageCapture.startAnimation(AnimationUtils.loadAnimation(applicationContext, android.R.anim.fade_out))

            startImageCapture(this@PublicCommunity)

        }

    }

    override fun onResume() {
        super.onResume()

        firebaseRecyclerAdapter.startListening()

    }

    override fun onPause() {
        super.onPause()

        firebaseRecyclerAdapter.stopListening()

    }

    override fun onBackPressed() {

        this@PublicCommunity.finish()
        overridePendingTransition(R.anim.fade_in, R.anim.slide_out_right)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)

        when (requestCode) {
            IMAGE_PICKER_REQUEST_CODE -> {

                if (resultCode == Activity.RESULT_OK) {

                    Handler(Looper.getMainLooper()).postDelayed({

                        publicCommunityViewBinding.addImageView.apply {
                            setMinAndMaxFrame(41, 75)
                        }.playAnimation()

                    }, 777)

                    publicCommunityViewBinding.imageMessageContentView.visibility = View.VISIBLE

                    val selectedImage: Uri? = resultData?.data

                    Glide.with(applicationContext)
                        .load(selectedImage)
                        .listener(object : RequestListener<Drawable> {

                            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {

                                return false
                            }

                            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {

                                resource?.let {

                                    listOfSelectedImages.add(resource)

                                    runOnUiThread {

                                        renderSelectedImagePreview(publicCommunityViewBinding, listOfSelectedImages, resource)

                                    }

                                }

                                return false
                            }

                        })
                        .submit()

                }

            }
            IMAGE_CAPTURE_REQUEST_CODE -> {

                if (resultCode == Activity.RESULT_OK) {

                    Handler(Looper.getMainLooper()).postDelayed({

                        publicCommunityViewBinding.addImageView.apply {
                            setMinAndMaxFrame(41, 75)
                        }.playAnimation()

                    }, 777)

                    publicCommunityViewBinding.imageMessageContentView.visibility = View.VISIBLE

                    val selectedImage = resultData?.extras?.get("data") as Bitmap

                    Glide.with(applicationContext)
                        .load(selectedImage)
                        .listener(object : RequestListener<Drawable> {

                            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {

                                return false
                            }

                            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {

                                resource?.let {

                                    listOfSelectedImages.add(resource)

                                    runOnUiThread {

                                        renderSelectedImagePreview(publicCommunityViewBinding, listOfSelectedImages, resource)

                                    }

                                }

                                return false
                            }

                        })
                        .submit()



                }

            }
        }

    }

    override fun networkAvailable() {

    }

    override fun networkLost() {

    }

}