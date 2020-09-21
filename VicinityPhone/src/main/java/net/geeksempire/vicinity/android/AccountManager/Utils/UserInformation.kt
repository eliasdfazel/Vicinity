/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 9/21/20 9:01 AM
 * Last modified 9/21/20 8:31 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.AccountManager.Utils

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import net.geeksempire.vicinity.android.AccountManager.UI.AccountInformation
import net.geeksempire.vicinity.android.R

class UserInformation(private val context: AccountInformation) {

    companion object {
        const val GoogleSignInRequestCode = 103

        fun allUsersInformationDatabasePath(vicinityDatabasePath: String) : String {

            return vicinityDatabasePath + "/" + "People"
        }

        fun uniqueUserInformationDatabasePath(vicinityDatabasePath: String, userIdentification: String) : String {

            return vicinityDatabasePath + "/" + "People" + "/" + userIdentification
        }

        fun userProfileDatabasePath(userUniqueIdentifier: String) : String = "Vicinity/UserInformation/${userUniqueIdentifier}/Profile"

        fun userVicinityArchiveDatabasePath(userUniqueIdentifier: String, vicinityName: String) : String = "Vicinity/UserInformation/${userUniqueIdentifier}/History/PublicCommunity/${vicinityName}"

        fun  userPrivateMessengerArchiveDatabasePath(userUniqueIdentifier: String) : String = "Vicinity/UserInformation/${userUniqueIdentifier}/History/PrivateMessenger"

    }

    fun startSignInProcess() {

        if (context.firebaseAuth.currentUser == null) {

            val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.webClientId))
                .requestEmail()
                .build()

            val googleSignInClient = GoogleSignIn.getClient(context, googleSignInOptions)
            googleSignInClient.signInIntent.run {
                context.startActivityForResult(this@run, GoogleSignInRequestCode)
            }

        }

    }

}