/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 11/19/20 11:29 AM
 * Last modified 11/19/20 11:24 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.CommunicationConfiguration.Private.PrivateMessengerUI

import android.animation.ValueAnimator
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
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
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
import net.geeksempire.vicinity.android.AccountManager.DataStructure.UserInformationDataStructure
import net.geeksempire.vicinity.android.AccountManager.Utils.UserInformation
import net.geeksempire.vicinity.android.CommunicationConfiguration.Private.DataStructure.PrivateMessageData
import net.geeksempire.vicinity.android.CommunicationConfiguration.Private.DataStructure.PrivateMessengerData
import net.geeksempire.vicinity.android.CommunicationConfiguration.Private.Endpoint.PrivateCommunicationEndpoint
import net.geeksempire.vicinity.android.CommunicationConfiguration.Private.Extensions.privateMessengerPrepareMessage
import net.geeksempire.vicinity.android.CommunicationConfiguration.Private.Extensions.privateMessengerPrepareNotificationData
import net.geeksempire.vicinity.android.CommunicationConfiguration.Private.Extensions.privateMessengerPrepareNotificationTopic
import net.geeksempire.vicinity.android.CommunicationConfiguration.Private.Extensions.privateMessengerSetupUI
import net.geeksempire.vicinity.android.CommunicationConfiguration.Private.PrivateMessengerUI.Adapter.PrivateMessengerAdapter
import net.geeksempire.vicinity.android.CommunicationConfiguration.Utils.ImageMessage.UI.MessageImagesViewer
import net.geeksempire.vicinity.android.CommunicationConfiguration.Utils.ImageMessage.Utils.*
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
import net.geeksempire.vicinity.android.databinding.PrivateMessengerViewBinding
import javax.inject.Inject
import kotlin.math.roundToInt

class PrivateMessenger : AppCompatActivity(), NetworkConnectionListenerInterface {

    object Configurations {
        const val PrivateMessengerName: String = "PrivateMessengerName"
        const val OtherUid: String = "OtherUid"
        const val OtherUsername: String = "OtherUsername"
        const val OtherProfileImage: String = "OtherProfileImage"
        const val PrivateMessengerDatabasePath: String = "PrivateMessengerDatabasePath"
        const val NotificationCloudFunction: String = "privateNewMessageNotification"
    }

    companion object {

        fun open(context: Context, privateMessengerName: String,
                 otherUid: String,
                 otherUsername: String,
                 otherProfileImage: String) {

            context.startActivity(Intent(context, PrivateMessenger::class.java).apply {
                putExtra(PrivateMessenger.Configurations.PrivateMessengerName, privateMessengerName)
                putExtra(PrivateMessenger.Configurations.OtherUid, otherUid)
                putExtra(PrivateMessenger.Configurations.OtherUsername, otherUsername)
                putExtra(PrivateMessenger.Configurations.OtherProfileImage, otherProfileImage)
                putExtra(PrivateMessenger.Configurations.PrivateMessengerDatabasePath, PrivateCommunicationEndpoint.privateMessengerDocumentEndpoint(privateMessengerName))
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }, ActivityOptions.makeCustomAnimation(context, R.anim.slide_in_right, R.anim.fade_out).toBundle())

        }

    }

    val overallTheme: OverallTheme by lazy {
        OverallTheme(applicationContext)
    }

    val firestoreDatabase: FirebaseFirestore = Firebase.firestore

    val firebaseUser: FirebaseUser = Firebase.auth.currentUser!!

    private lateinit var firebaseRecyclerAdapter: FirestoreRecyclerAdapter<PrivateMessageData, RecyclerView.ViewHolder>

    val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(this@PrivateMessenger, RecyclerView.VERTICAL, false)
    }

    private val firebaseCloudFunctions: FirebaseFunctions = FirebaseFunctions.getInstance()

    val listOfSelectedImages: ArrayList<Drawable> = ArrayList<Drawable>(3)

    val messageImagesViewer = MessageImagesViewer()

    var sentMessagePathForImages: String? = null

    @Inject lateinit var networkCheckpoint: NetworkCheckpoint

    @Inject lateinit var networkConnectionListener: NetworkConnectionListener

    lateinit var privateMessengerViewBinding: PrivateMessengerViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        privateMessengerViewBinding = PrivateMessengerViewBinding.inflate(layoutInflater)
        setContentView(privateMessengerViewBinding.root)

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
            .create(this@PrivateMessenger, privateMessengerViewBinding.rootView)
            .inject(this@PrivateMessenger)

        networkConnectionListener.networkConnectionListenerInterface = this@PrivateMessenger

        val otherUid = intent.getStringExtra(PrivateMessenger.Configurations.OtherUid)!!
        val otherUsername = intent.getStringExtra(PrivateMessenger.Configurations.OtherUsername)!!
        val otherProfileImage = intent.getStringExtra(PrivateMessenger.Configurations.OtherProfileImage)!!

        val privateMessengerName = intent.getStringExtra(PrivateMessenger.Configurations.PrivateMessengerName)

        val privateMessagesDatabasePath: String = intent.getStringExtra(PrivateMessenger.Configurations.PrivateMessengerDatabasePath).plus("/Messages")

        Handler(Looper.getMainLooper()).postDelayed({

            firestoreDatabase
                .document(UserInformation.userProfileDatabasePath(otherUid))
                .get()
                .addOnSuccessListener { documentSnapshot ->

                    Glide.with(applicationContext)
                        .load(documentSnapshot[UserInformationDataStructure.userProfileImage].toString())
                        .transform(CircleCrop())
                        .listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {

                                return false
                            }

                            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {

                                runOnUiThread {

                                    privateMessengerViewBinding.vicinityFriendName.icon = resource

                                    privateMessengerViewBinding.vicinityFriendName.text = "${documentSnapshot[UserInformationDataStructure.userDisplayName]}"

                                    privateMessengerViewBinding.vicinityFriendName.post {

                                        privateMessengerViewBinding.vicinityFriendName.visibility = View.VISIBLE

                                        val valueAnimatorKnownName = ValueAnimator.ofInt(
                                            1,
                                            privateMessengerViewBinding.vicinityFriendName.width
                                        )
                                        valueAnimatorKnownName.duration = 777
                                        valueAnimatorKnownName.addUpdateListener { animator ->
                                            val animatorValue = animator.animatedValue as Int

                                            val vicinityFriendNameLayoutParams =
                                                privateMessengerViewBinding.vicinityFriendName.layoutParams as ConstraintLayout.LayoutParams
                                            vicinityFriendNameLayoutParams.width = animatorValue
                                            privateMessengerViewBinding.vicinityFriendName.layoutParams =
                                                vicinityFriendNameLayoutParams

                                        }
                                        valueAnimatorKnownName.start()

                                    }

                                }

                                return false
                            }

                        })
                        .submit()

                }.addOnFailureListener {


                }

        }, 555)

        privateMessengerName?.let {

            FirebaseMessaging.getInstance().subscribeToTopic(privateMessengerPrepareNotificationTopic(privateMessengerName))
                .addOnSuccessListener {
                    Log.d(this@PrivateMessenger.javaClass.simpleName, "Subscribed To ${privateMessengerName}")

                }.addOnFailureListener {
                    Log.d(this@PrivateMessenger.javaClass.simpleName, "Failed To Subscribe")

                }

            intent.getStringExtra(PrivateMessenger.Configurations.PrivateMessengerDatabasePath)?.let {

                val privateMessengerData = PrivateMessengerData(
                    PersonOne = firebaseUser.uid,
                    PersonOneUsername = firebaseUser.displayName!!,
                    PersonOneProfileImage = firebaseUser.photoUrl.toString(),
                    PersonTwo = otherUid,
                    PersonTwoUsername = otherUsername,
                    PersonTwoProfileImage = otherProfileImage
                )

                firestoreDatabase
                    .document(it)
                    .set(privateMessengerData)

                firestoreDatabase
                    .document(UserInformation.userPrivateMessengerArchiveDatabasePath(firebaseUser.uid, privateMessengerName))
                    .set(privateMessengerData)

                firestoreDatabase
                    .document(UserInformation.userPrivateMessengerArchiveDatabasePath(otherUid, privateMessengerName))
                    .set(privateMessengerData)

            }

        }

        privateMessengerSetupUI()

        linearLayoutManager.stackFromEnd = false

        val publicMessageCollectionReference: Query = firestoreDatabase
            .collection(privateMessagesDatabasePath)
            .orderBy("userMessageDate", Query.Direction.ASCENDING)

        val firebaseRecyclerOptions = FirestoreRecyclerOptions.Builder<PrivateMessageData>()
            .setQuery(publicMessageCollectionReference, PrivateMessageData::class.java)
            .build()

        firebaseRecyclerAdapter = PrivateMessengerAdapter(this@PrivateMessenger, firebaseRecyclerOptions)

        privateMessengerViewBinding.messageRecyclerView.layoutManager = linearLayoutManager
        privateMessengerViewBinding.messageRecyclerView.adapter = firebaseRecyclerAdapter

        firebaseRecyclerAdapter.registerAdapterDataObserver(object :
            RecyclerView.AdapterDataObserver() {

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)

                val friendlyMessageCount = firebaseRecyclerAdapter.itemCount
                val lastVisiblePosition = linearLayoutManager.findLastCompletelyVisibleItemPosition()

                if (lastVisiblePosition == -1 || positionStart >= friendlyMessageCount - 1 && lastVisiblePosition == positionStart - 1) {
                    Handler(Looper.getMainLooper()).postDelayed({

                        privateMessengerViewBinding.nestedScrollView.smoothScrollTo(0, privateMessengerViewBinding.messageRecyclerView.height)

                        privateMessengerViewBinding.loadingView.visibility = View.GONE

                    }, 500)
                }

            }

        })

        privateMessengerViewBinding.sendMessageView.setOnClickListener {

            if (privateMessengerViewBinding.textMessageContentView.text.toString().isNotEmpty()
                || privateMessengerViewBinding.imageMessageContentView.isShown) {

                val publicCommunityPrepareMessage = privateMessengerPrepareMessage()
                firestoreDatabase
                    .collection(privateMessagesDatabasePath)
                    .add(publicCommunityPrepareMessage)
                    .addOnSuccessListener { documentSnapshot ->

                        if (privateMessengerViewBinding.imageMessageContentView.isShown) {

                            val sentMessagePath = privateMessagesDatabasePath + "/" + documentSnapshot.id

                            sentMessagePathForImages = PrivateCommunicationEndpoint.privateMessengerStoragePreviewImageEndpoint(
                                privateMessengerMessagesDatabasePath = privateMessagesDatabasePath,
                                documentSnapshotId = documentSnapshot.id)

                            bitmapToByteArray(takeViewSnapshot(privateMessengerViewBinding.imageMessageContentView))?.let { drawableByteArray ->

                                val firebaseStorage = Firebase.storage
                                val storageReference = firebaseStorage.reference
                                    .child(sentMessagePathForImages!!)
                                storageReference
                                    .putBytes(drawableByteArray)
                                    .addOnSuccessListener {

                                        storageReference.downloadUrl.addOnSuccessListener { imageDownloadLink ->

                                            firestoreDatabase
                                                .document(sentMessagePath)
                                                .update(
                                                    "userMessageImageContent", imageDownloadLink.toString(),
                                                    "privateMessengerStorageImagesItemEndpoint", PrivateCommunicationEndpoint.privateMessengerStorageImagesItemEndpoint(privateMessagesDatabasePath, documentSnapshot.id),
                                                ).addOnSuccessListener {

                                                    val messageContent = privateMessengerViewBinding.textMessageContentView.text.toString()

                                                    if (privateMessengerName != null) {

                                                        sendNotificationToOthers(messageContent, privateMessengerName)

                                                    }

                                                }

                                        }

                                    }

                            }

                            repeat(listOfSelectedImages.size) { imageIndex ->

                                val sentMessagePathForImages = PrivateCommunicationEndpoint.privateMessengerStorageImagesItemEndpoint(
                                    privateMessengerMessagesDatabasePath = privateMessagesDatabasePath,
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

                        }

                        privateMessengerViewBinding.sendMessageView.setAnimation(R.raw.sending_animation)
                        privateMessengerViewBinding.sendMessageView.playAnimation()
                        privateMessengerViewBinding.sendMessageView.addAnimatorUpdateListener { valueAnimator ->

                            val animationProgress = (valueAnimator.animatedValue as Float * 100).roundToInt()

                            if (animationProgress == 96) {

                                Handler(Looper.getMainLooper()).postDelayed({

                                    val animationSpeed = privateMessengerViewBinding.sendMessageView.speed

                                    privateMessengerViewBinding.sendMessageView.speed = -(animationSpeed)
                                    privateMessengerViewBinding.sendMessageView.playAnimation()

                                }, 157)

                            }

                        }

                        val messageContent = privateMessengerViewBinding.textMessageContentView.text.toString()

                        if (privateMessengerName != null) {

                            if (sentMessagePathForImages != null) {



                            } else {

                                sendNotificationToOthers(messageContent, privateMessengerName)


                            }

                        }

                        privateMessengerViewBinding.textMessageContentView.text = null
                        privateMessengerViewBinding.imageMessageContentOne.setImageDrawable(null)
                        privateMessengerViewBinding.imageMessageContentTwo.setImageDrawable(null)
                        privateMessengerViewBinding.imageMessageContentThree.setImageDrawable(null)
                        privateMessengerViewBinding.imageMessageContentView.visibility = View.GONE

                        privateMessengerViewBinding.nestedScrollView.smoothScrollTo(
                            0,
                            privateMessengerViewBinding.messageRecyclerView.height
                        )

                    }.addOnFailureListener {

                        privateMessengerViewBinding.textMessageContentLayout.error = getString(R.string.messageSentError)
                        privateMessengerViewBinding.textMessageContentLayout.isErrorEnabled = true

                        privateMessengerViewBinding.sendMessageView.setAnimation(R.raw.sending_animation_error)
                        privateMessengerViewBinding.sendMessageView.playAnimation()

                        privateMessengerViewBinding.sendMessageView.addAnimatorUpdateListener { valueAnimator ->

                            val animationProgress = (valueAnimator.animatedValue as Float * 100).roundToInt()

                            if (animationProgress == 96) {

                                privateMessengerViewBinding.sendMessageView.setAnimation(R.raw.sending_animation)

                            }

                        }

                    }

            }

        }

        privateMessengerViewBinding.addImageView.setOnClickListener {

            privateMessengerViewBinding.addImageView.isEnabled = false

            if (privateMessengerViewBinding.imageGallery.isShown && privateMessengerViewBinding.imageCapture.isShown) {

                privateMessengerViewBinding.imageGallery.startAnimation(
                    TranslateAnimation(
                    0f,
                    -(DpToInteger(applicationContext, 51)).toFloat(),
                    0f,
                    0f
                ).apply {
                    duration = 379
                })

                privateMessengerViewBinding.imageCapture.startAnimation(
                    TranslateAnimation(
                    0f,
                    -(DpToInteger(applicationContext, 101)).toFloat(),
                    0f,
                    0f
                ).apply {
                    duration = 379
                })

                Handler(Looper.getMainLooper()).postDelayed({

                    privateMessengerViewBinding.imageGallery.visibility = View.INVISIBLE
                    privateMessengerViewBinding.imageGallery.startAnimation(AnimationUtils.loadAnimation(applicationContext, android.R.anim.fade_out))

                    privateMessengerViewBinding.imageCapture.visibility = View.INVISIBLE
                    privateMessengerViewBinding.imageCapture.startAnimation(AnimationUtils.loadAnimation(applicationContext, android.R.anim.fade_out))

                    privateMessengerViewBinding.addImageView.apply {
                        setMinAndMaxFrame(41, 75)
                    }.playAnimation()

                }, 379)

            } else {

                privateMessengerViewBinding.addImageView.apply {
                    setMinAndMaxFrame(0, 41)
                }.playAnimation()
                privateMessengerViewBinding.addImageView.addAnimatorUpdateListener { valueAnimator ->

                    val animationProgress = (valueAnimator.animatedValue as Float * 100).roundToInt()

                    if (animationProgress == 49) {

                        privateMessengerViewBinding.imageGallery.visibility = View.VISIBLE
                        privateMessengerViewBinding.imageGallery.startAnimation(
                            TranslateAnimation(
                            -(DpToInteger(applicationContext, 51)).toFloat(),
                            0f,
                            0f,
                            0f
                        ).apply {
                            duration = 555
                        })

                        privateMessengerViewBinding.imageCapture.visibility = View.VISIBLE
                        privateMessengerViewBinding.imageCapture.startAnimation(
                            TranslateAnimation(
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
                privateMessengerViewBinding.addImageView.isEnabled = true
            }, 1531)

        }

        privateMessengerViewBinding.imageGallery.setOnClickListener {

            privateMessengerViewBinding.imageGallery.visibility = View.INVISIBLE
            privateMessengerViewBinding.imageGallery.startAnimation(AnimationUtils.loadAnimation(applicationContext, android.R.anim.fade_out))

            privateMessengerViewBinding.imageCapture.visibility = View.INVISIBLE
            privateMessengerViewBinding.imageCapture.startAnimation(AnimationUtils.loadAnimation(applicationContext, android.R.anim.fade_out))

            startImagePicker(this@PrivateMessenger)

        }

        privateMessengerViewBinding.imageCapture.setOnClickListener {

            privateMessengerViewBinding.imageGallery.visibility = View.INVISIBLE
            privateMessengerViewBinding.imageGallery.startAnimation(AnimationUtils.loadAnimation(applicationContext, android.R.anim.fade_out))

            privateMessengerViewBinding.imageCapture.visibility = View.INVISIBLE
            privateMessengerViewBinding.imageCapture.startAnimation(AnimationUtils.loadAnimation(applicationContext, android.R.anim.fade_out))

            startImageCapture(this@PrivateMessenger)

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

        if (privateMessengerViewBinding.fragmentContainer.isShown) {

            privateMessengerViewBinding.fragmentContainer.startAnimation(AnimationUtils.loadAnimation(applicationContext, R.anim.slide_out_right))
            privateMessengerViewBinding.fragmentContainer.visibility = View.GONE

            supportFragmentManager.beginTransaction().detach(messageImagesViewer)

        } else {

            this@PrivateMessenger.finish()
            overridePendingTransition(R.anim.fade_in, R.anim.slide_out_right)

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)

        when (requestCode) {
            IMAGE_PICKER_REQUEST_CODE -> {

                if (resultCode == Activity.RESULT_OK) {

                    Handler(Looper.getMainLooper()).postDelayed({

                        privateMessengerViewBinding.addImageView.apply {
                            setMinAndMaxFrame(41, 75)
                        }.playAnimation()

                    }, 777)

                    privateMessengerViewBinding.imageMessageContentView.visibility = View.VISIBLE

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

                                        renderSelectedImagePreview(privateMessengerViewBinding, listOfSelectedImages, resource)

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

                        privateMessengerViewBinding.addImageView.apply {
                            setMinAndMaxFrame(41, 75)
                        }.playAnimation()

                    }, 777)

                    privateMessengerViewBinding.imageMessageContentView.visibility = View.VISIBLE

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

                                        renderSelectedImagePreview(privateMessengerViewBinding, listOfSelectedImages, resource)

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

    private fun sendNotificationToOthers(messageContent: String, privateMessengerName: String) {

        firebaseCloudFunctions
            .getHttpsCallable(PrivateMessenger.Configurations.NotificationCloudFunction)
            .call(privateMessengerPrepareNotificationData(messageContent, privateMessengerName))
            .continueWith {

            }

    }

}