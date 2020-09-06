/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 9/6/20 7:50 AM
 * Last modified 9/6/20 7:45 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.MapConfiguration.Vicinity.Operations

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import net.geeksempire.vicinity.android.AccountManager.Data.UserInformationData
import net.geeksempire.vicinity.android.AccountManager.UserInformation

class AddUserInformationToVicinity (private val firestoreDatabase: FirebaseFirestore) {

    fun add(vicinityDatabasePath: String, userInformationData: UserInformationData) {

        firestoreDatabase
            .document(UserInformation.userInformationDatabasePath(vicinityDatabasePath, userInformationData.userIdentification))
            .set(userInformationData)
            .addOnSuccessListener {
                Log.d(this@AddUserInformationToVicinity.javaClass.simpleName, "User Added To ${vicinityDatabasePath}")

            }
            .addOnFailureListener {
                Log.d(this@AddUserInformationToVicinity.javaClass.simpleName, "Failed To Add User")

            }

    }

}