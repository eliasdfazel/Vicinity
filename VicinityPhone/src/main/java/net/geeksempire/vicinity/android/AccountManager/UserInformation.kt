/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 9/6/20 8:36 AM
 * Last modified 9/6/20 8:21 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.AccountManager

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import net.geeksempire.vicinity.android.R


class UserInformation(private val context: AccountSignIn) {

    companion object {
        const val GoogleSignInRequestCode = 103

        fun allUsersInformationDatabasePath(vicinityDatabasePath: String) : String {

            return vicinityDatabasePath + "/" + "People"
        }

        fun uniqueUserInformationDatabasePath(vicinityDatabasePath: String, userIdentification: String) : String {

            return vicinityDatabasePath + "/" + "People" + "/" + userIdentification
        }
    }

    fun startSignInProcess() {

        if (context.firebaseAuth.currentUser == null) {

            val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.webClientId))
                .requestEmail()
                .build()

            val googleSignInClient = GoogleSignIn.getClient(context, googleSignInOptions)
            googleSignInClient.signInIntent.run {
                context.startActivityForResult(this@run, UserInformation.GoogleSignInRequestCode)
            }

        }

    }

}