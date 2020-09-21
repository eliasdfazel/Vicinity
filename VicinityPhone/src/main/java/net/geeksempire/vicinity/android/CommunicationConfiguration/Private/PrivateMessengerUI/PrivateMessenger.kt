/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 9/21/20 9:01 AM
 * Last modified 9/21/20 9:01 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.CommunicationConfiguration.Private.PrivateMessengerUI

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase

class PrivateMessenger : AppCompatActivity() {

    val firestoreDatabase: FirebaseFirestore = Firebase.firestore

    val firebaseUser: FirebaseUser = Firebase.auth.currentUser!!

    var otherUid: String? = null

    var privateMessengerName: String? = null

    companion object {

        fun open(context: Context, privateMessengerName: String, otherUid: String) {

            val privateMessengerIntent = Intent().apply {
                putExtra("PrivateMessengerName", privateMessengerName)
                putExtra("OtherUid", otherUid)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(privateMessengerIntent)

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            val firebaseFirestoreSettings = firestoreSettings {
                isPersistenceEnabled = false
                cacheSizeBytes = FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED
            }

            firestoreDatabase.firestoreSettings = firebaseFirestoreSettings
        } catch (e: Exception) {
            e.printStackTrace()
        }

        otherUid = intent.getStringExtra("OtherUid")

        privateMessengerName = intent.getStringExtra("PrivateMessengerName")



    }

}