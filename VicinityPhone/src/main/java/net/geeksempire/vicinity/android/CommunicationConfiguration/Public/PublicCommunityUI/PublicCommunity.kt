/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 9/10/20 9:38 AM
 * Last modified 9/10/20 9:29 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.CommunicationConfiguration.Public.PublicCommunityUI

import android.os.Bundle
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
import net.geeksempire.vicinity.android.CommunicationConfiguration.Public.DataStructure.PublicMessageData
import net.geeksempire.vicinity.android.CommunicationConfiguration.Public.Extensions.publicCommunityPrepareMessage
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

class PublicCommunity : AppCompatActivity(), NetworkConnectionListenerInterface {

    object Configurations {
        const val PublicCommunityName: String = "PublicCommunityName"
        const val PublicCommunityDatabasePath: String = "PublicCommunityDatabasePath"
    }

    val overallTheme: OverallTheme by lazy {
        OverallTheme(applicationContext)
    }

    val firestoreDatabase: FirebaseFirestore = Firebase.firestore

    val firebaseUser: FirebaseUser = Firebase.auth.currentUser!!

    private lateinit var firebaseRecyclerAdapter: FirestoreRecyclerAdapter<PublicMessageData, PublicCommunityViewHolder>

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

        val publicCommunityName: String = intent.getStringExtra(PublicCommunity.Configurations.PublicCommunityName)
        val publicCommunityMessagesDatabasePath: String = intent.getStringExtra(PublicCommunity.Configurations.PublicCommunityDatabasePath).plus("/Messages")

        FirebaseMessaging.getInstance().subscribeToTopic(publicCommunityName)
            .addOnSuccessListener {

            }.addOnFailureListener {

            }

        publicCommunitySetupUI()

        val linearLayoutManager = LinearLayoutManager(this@PublicCommunity, RecyclerView.VERTICAL, false)
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

        publicCommunityViewBinding.sendMessageView.setOnClickListener {

            if (publicCommunityViewBinding.textMessageContentView.text.toString().isNotEmpty()) {

                firestoreDatabase
                    .collection(publicCommunityMessagesDatabasePath)
                    .add(publicCommunityPrepareMessage())
                    .addOnSuccessListener {

                        publicCommunityViewBinding.textMessageContentView.text = null

                        val linkedHashMapData: LinkedHashMap<Any, Any> = LinkedHashMap<Any, Any>()
                        linkedHashMapData["notificationTopic"] = publicCommunityName
                        linkedHashMapData["selfUid"] = firebaseUser.uid
                        linkedHashMapData["selfDisplayName"] = firebaseUser.displayName.toString()
                        linkedHashMapData["publicCommunityAction"] = getString(R.string.publicCommunityAction)
                        linkedHashMapData["publicCommunityName"] = publicCommunityName
                        linkedHashMapData["notificationLargeIcon"] = firebaseUser.photoUrl.toString()
                        linkedHashMapData["messageContent"] = publicCommunityViewBinding.textMessageContentView.text.toString()

                        firebaseCloudFunctions
                            .getHttpsCallable("publicCommunityNewMessageNotification")
                            .call(linkedHashMapData)

                    }.addOnFailureListener {

                        publicCommunityViewBinding.textMessageContentLayout.error = getString(R.string.messageSentError)
                        publicCommunityViewBinding.textMessageContentLayout.isErrorEnabled = true

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