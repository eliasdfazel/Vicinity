/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 9/21/20 10:47 AM
 * Last modified 9/21/20 10:46 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.CommunicationConfiguration.Private.PrivateMessengerUI

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import net.geeksempire.vicinity.android.CommunicationConfiguration.Private.Endpoint.PrivateCommunicationEndpoint
import net.geeksempire.vicinity.android.CommunicationConfiguration.Private.Extensions.privateMessengerPrepareMessage
import net.geeksempire.vicinity.android.CommunicationConfiguration.Private.Extensions.privateMessengerPrepareNotificationData
import net.geeksempire.vicinity.android.CommunicationConfiguration.Private.Extensions.privateMessengerPrepareNotificationTopic
import net.geeksempire.vicinity.android.CommunicationConfiguration.Private.Extensions.privateMessengerSetupUI
import net.geeksempire.vicinity.android.CommunicationConfiguration.Private.PrivateMessengerUI.Adapter.PrivateMessengerAdapter
import net.geeksempire.vicinity.android.CommunicationConfiguration.Public.DataStructure.PublicMessageData
import net.geeksempire.vicinity.android.R
import net.geeksempire.vicinity.android.Utils.Networking.NetworkCheckpoint
import net.geeksempire.vicinity.android.Utils.Networking.NetworkConnectionListener
import net.geeksempire.vicinity.android.Utils.Networking.NetworkConnectionListenerInterface
import net.geeksempire.vicinity.android.Utils.UI.Theme.OverallTheme
import net.geeksempire.vicinity.android.VicinityApplication
import net.geeksempire.vicinity.android.databinding.PrivateMessengerViewBinding
import javax.inject.Inject
import kotlin.math.roundToInt

class PrivateMessenger : AppCompatActivity(), NetworkConnectionListenerInterface {

    object Configurations {
        const val PrivateMessengerName: String = "PrivateMessengerName"
        const val OtherUid: String = "OtherUid"
        const val PrivateMessengerDatabasePath: String = "PrivateMessengerDatabasePath"

        const val NotificationCloudFunction: String = "privateNewMessageNotification"
    }

    companion object {

        fun open(context: Context, privateMessengerName: String, otherUid: String) {

            context.startActivity(Intent(context, PrivateMessenger::class.java).apply {
                putExtra(PrivateMessenger.Configurations.PrivateMessengerName, privateMessengerName)
                putExtra(PrivateMessenger.Configurations.OtherUid, otherUid)
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

    private lateinit var firebaseRecyclerAdapter: FirestoreRecyclerAdapter<PublicMessageData, RecyclerView.ViewHolder>

    val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(this@PrivateMessenger, RecyclerView.VERTICAL, false)
    }

    private val firebaseCloudFunctions: FirebaseFunctions = FirebaseFunctions.getInstance()

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

        val otherUid = intent.getStringExtra(PrivateMessenger.Configurations.OtherUid)

        val privateMessengerName = intent.getStringExtra(PrivateMessenger.Configurations.PrivateMessengerName)

        val privateMessagesDatabasePath: String = intent.getStringExtra(PrivateMessenger.Configurations.PrivateMessengerDatabasePath).plus("/Messages")

        privateMessengerName?.let {

            FirebaseMessaging.getInstance().subscribeToTopic(privateMessengerPrepareNotificationTopic(privateMessengerName))
                .addOnSuccessListener {
                    Log.d(this@PrivateMessenger.javaClass.simpleName, "Subscribed To ${privateMessengerName}")

                }.addOnFailureListener {
                    Log.d(this@PrivateMessenger.javaClass.simpleName, "Failed To Subscribe")

                }

        }

        privateMessengerSetupUI()

        linearLayoutManager.stackFromEnd = false

        val publicMessageCollectionReference: Query = firestoreDatabase
            .collection(privateMessagesDatabasePath)
            .orderBy("userMessageDate", Query.Direction.ASCENDING)

        val firebaseRecyclerOptions = FirestoreRecyclerOptions.Builder<PublicMessageData>()
            .setQuery(publicMessageCollectionReference, PublicMessageData::class.java)
            .build()

        firebaseRecyclerAdapter = PrivateMessengerAdapter(this@PrivateMessenger, firebaseRecyclerOptions)

        privateMessengerViewBinding.messageRecyclerView.layoutManager = linearLayoutManager
        privateMessengerViewBinding.messageRecyclerView.adapter = firebaseRecyclerAdapter

        firebaseRecyclerAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {

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

            if (privateMessengerViewBinding.textMessageContentView.text.toString().isNotEmpty()) {

                firestoreDatabase
                    .collection(privateMessagesDatabasePath)
                    .add(privateMessengerPrepareMessage())
                    .addOnSuccessListener {

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

                            firebaseCloudFunctions
                                .getHttpsCallable(PrivateMessenger.Configurations.NotificationCloudFunction)
                                .call(privateMessengerPrepareNotificationData(messageContent, privateMessengerName))
                                .continueWith {

                                }

                        }

                        privateMessengerViewBinding.textMessageContentView.text = null

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

        this@PrivateMessenger.finish()
        overridePendingTransition(R.anim.fade_in, R.anim.slide_out_right)

    }

    override fun networkAvailable() {

    }

    override fun networkLost() {

    }

}