/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 9/17/20 10:19 AM
 * Last modified 9/17/20 10:17 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.CommunicationConfiguration.Public.PublicCommunityUI

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
import net.geeksempire.vicinity.android.CommunicationConfiguration.Public.DataStructure.PublicMessageData
import net.geeksempire.vicinity.android.CommunicationConfiguration.Public.Extensions.publicCommunityPrepareMessage
import net.geeksempire.vicinity.android.CommunicationConfiguration.Public.Extensions.publicCommunityPrepareNotificationData
import net.geeksempire.vicinity.android.CommunicationConfiguration.Public.Extensions.publicCommunityPrepareNotificationTopic
import net.geeksempire.vicinity.android.CommunicationConfiguration.Public.Extensions.publicCommunitySetupUI
import net.geeksempire.vicinity.android.CommunicationConfiguration.Public.PublicCommunityUI.Adapter.PublicCommunityAdapter
import net.geeksempire.vicinity.android.CommunicationConfiguration.Public.PublicCommunityUI.Adapter.PublicCommunityViewHolder
import net.geeksempire.vicinity.android.R
import net.geeksempire.vicinity.android.Utils.Networking.NetworkCheckpoint
import net.geeksempire.vicinity.android.Utils.Networking.NetworkConnectionListener
import net.geeksempire.vicinity.android.Utils.Networking.NetworkConnectionListenerInterface
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
    }

    val overallTheme: OverallTheme by lazy {
        OverallTheme(applicationContext)
    }

    val firestoreDatabase: FirebaseFirestore = Firebase.firestore

    val firebaseUser: FirebaseUser = Firebase.auth.currentUser!!

    private lateinit var firebaseRecyclerAdapter: FirestoreRecyclerAdapter<PublicMessageData, PublicCommunityViewHolder>

    val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(this@PublicCommunity, RecyclerView.VERTICAL, false)
    }

    private val firebaseCloudFunctions: FirebaseFunctions = FirebaseFunctions.getInstance()

    @Inject lateinit var networkCheckpoint: NetworkCheckpoint

    @Inject lateinit var networkConnectionListener: NetworkConnectionListener

    lateinit var publicCommunityViewBinding: PublicCommunityViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        publicCommunityViewBinding = PublicCommunityViewBinding.inflate(layoutInflater)
        setContentView(publicCommunityViewBinding.root)

        (application as VicinityApplication)
            .dependencyGraph
            .subDependencyGraph()
            .create(this@PublicCommunity, publicCommunityViewBinding.rootView)
            .inject(this@PublicCommunity)

        networkConnectionListener.networkConnectionListenerInterface = this@PublicCommunity

        val firebaseFirestoreSettings = firestoreSettings {
            isPersistenceEnabled = true
            cacheSizeBytes = FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED
        }

        firestoreDatabase.firestoreSettings = firebaseFirestoreSettings

        val publicCommunityName: String? = intent.getStringExtra(PublicCommunity.Configurations.PublicCommunityName)
        val publicCommunityMessagesDatabasePath: String = intent.getStringExtra(PublicCommunity.Configurations.PublicCommunityDatabasePath).plus("/Messages")

        val publicCommunityCountryName: String? = intent.getStringExtra(PublicCommunity.Configurations.PublicCommunityCountryName)

        val communityCenterVicinity = LatLng(
            intent.getDoubleExtra(PublicCommunity.Configurations.PublicCommunityCenterLocationLatitude, 0.0),
            intent.getDoubleExtra(PublicCommunity.Configurations.PublicCommunityCenterLocationLongitude, 0.0)
        )


        publicCommunityName?.let {

            FirebaseMessaging.getInstance().subscribeToTopic(publicCommunityPrepareNotificationTopic(publicCommunityName))
                .addOnSuccessListener {
                    Log.d(this@PublicCommunity.javaClass.simpleName, "Subscribed To ${publicCommunityName}")

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

        firebaseRecyclerAdapter = PublicCommunityAdapter(this@PublicCommunity, firebaseRecyclerOptions)

        publicCommunityViewBinding.messageRecyclerView.layoutManager = linearLayoutManager
        publicCommunityViewBinding.messageRecyclerView.adapter = firebaseRecyclerAdapter

        firebaseRecyclerAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)

                val friendlyMessageCount = firebaseRecyclerAdapter.itemCount
                val lastVisiblePosition = linearLayoutManager.findLastCompletelyVisibleItemPosition()

                if (lastVisiblePosition == -1 || positionStart >= friendlyMessageCount - 1 && lastVisiblePosition == positionStart - 1) {
                    Handler(Looper.getMainLooper()).postDelayed({

                        publicCommunityViewBinding.nestedScrollView.smoothScrollTo(0, publicCommunityViewBinding.messageRecyclerView.height)

                        publicCommunityViewBinding.loadingView.visibility = View.GONE

                    }, 500)
                }

            }

        })

        publicCommunityViewBinding.sendMessageView.setOnClickListener {

            if (publicCommunityViewBinding.textMessageContentView.text.toString().isNotEmpty()) {

                firestoreDatabase
                    .collection(publicCommunityMessagesDatabasePath)
                    .add(publicCommunityPrepareMessage())
                    .addOnSuccessListener {

                        publicCommunityViewBinding.sendMessageView.setAnimation(R.raw.sending_animation)
                        publicCommunityViewBinding.sendMessageView.playAnimation()
                        publicCommunityViewBinding.sendMessageView.addAnimatorUpdateListener { valueAnimator ->

                            val animationProgress = (valueAnimator.animatedValue as Float * 100).roundToInt()

                            if (animationProgress == 96) {

                                Handler(Looper.getMainLooper()).postDelayed({

                                    val animationSpeed = publicCommunityViewBinding.sendMessageView.speed

                                    publicCommunityViewBinding.sendMessageView.speed = -(animationSpeed)
                                    publicCommunityViewBinding.sendMessageView.playAnimation()

                                }, 157)

                            }

                        }

                        val messageContent = publicCommunityViewBinding.textMessageContentView.text.toString()

                        if (publicCommunityName != null && publicCommunityCountryName != null) {

                            firebaseCloudFunctions
                                .getHttpsCallable("publicCommunityNewMessageNotification")
                                .call(publicCommunityPrepareNotificationData(messageContent, publicCommunityName, publicCommunityCountryName, communityCenterVicinity))
                                .continueWith {

                                }

                        }

                        publicCommunityViewBinding.textMessageContentView.text = null

                        publicCommunityViewBinding.nestedScrollView.smoothScrollTo(
                            0,
                            publicCommunityViewBinding.messageRecyclerView.height
                        )

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

    override fun networkAvailable() {

    }

    override fun networkLost() {

    }

}