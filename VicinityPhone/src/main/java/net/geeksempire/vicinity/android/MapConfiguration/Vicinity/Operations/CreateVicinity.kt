/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 9/5/20 11:05 AM
 * Last modified 9/5/20 10:04 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.MapConfiguration.Vicinity.Operations

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

class CreateVicinity (private val firestoreDatabase: FirebaseFirestore) {

    fun create(vicinityDatabasePath: String, vicinityData: VicinityData) {

        firestoreDatabase
            .document(vicinityDatabasePath)
            .set(vicinityData)
            .addOnSuccessListener {
                Log.d(this@CreateVicinity.javaClass.simpleName, "Vicinity Created")

                //

            }.addOnFailureListener {
                Log.d(this@CreateVicinity.javaClass.simpleName, "Vicinity Failed To Create")

                //

            }

    }

}