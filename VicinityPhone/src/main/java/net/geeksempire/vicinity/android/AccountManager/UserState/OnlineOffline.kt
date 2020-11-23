/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 11/23/20 8:53 AM
 * Last modified 11/23/20 8:49 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.AccountManager.UserState

import com.google.firebase.firestore.FirebaseFirestore
import net.geeksempire.vicinity.android.AccountManager.Utils.UserInformation

class OnlineOffline (private val firestoreDatabase: FirebaseFirestore) {

    fun startUserStateProcess(vicinityDatabasePath: String?, userIdentification: String, userOnlineOfflineState: Boolean) {

        vicinityDatabasePath?.let {

            firestoreDatabase
                .document(UserInformation.uniqueUserInformationDatabasePath(vicinityDatabasePath, userIdentification))
                .update(
                    "userState", userOnlineOfflineState,
                ).addOnSuccessListener {



                }

        }

        firestoreDatabase
            .document(UserInformation.userProfileDatabasePath(userIdentification))
            .update(
                "userState", userOnlineOfflineState,
            ).addOnSuccessListener {



            }

    }

}