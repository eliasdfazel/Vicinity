/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 8/30/20 10:10 AM
 * Last modified 8/30/20 10:10 AM
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