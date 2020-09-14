/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 9/14/20 8:30 AM
 * Last modified 9/14/20 8:03 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.AccountManager.UI

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import net.geeksempire.vicinity.android.AccountManager.Extensions.accountManagerSetupUI
import net.geeksempire.vicinity.android.AccountManager.Utils.UserInformation
import net.geeksempire.vicinity.android.AccountManager.Utils.UserInformationIO
import net.geeksempire.vicinity.android.MapConfiguration.Map.MapsOfSociety
import net.geeksempire.vicinity.android.R
import net.geeksempire.vicinity.android.Utils.Networking.NetworkCheckpoint
import net.geeksempire.vicinity.android.Utils.Networking.NetworkConnectionListener
import net.geeksempire.vicinity.android.Utils.UI.Theme.OverallTheme
import net.geeksempire.vicinity.android.VicinityApplication
import net.geeksempire.vicinity.android.databinding.AccountViewBinding
import javax.inject.Inject

class AccountInformation : AppCompatActivity() {

    val overallTheme: OverallTheme by lazy {
        OverallTheme(applicationContext)
    }

    private val userInformation: UserInformation by lazy {
        UserInformation(this@AccountInformation)
    }

    private val userInformationIO: UserInformationIO by lazy {
        UserInformationIO(applicationContext)
    }

    val firebaseAuth = Firebase.auth

    @Inject lateinit var networkCheckpoint: NetworkCheckpoint

    @Inject lateinit var networkConnectionListener: NetworkConnectionListener

    lateinit var accountViewBinding: AccountViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        accountViewBinding = AccountViewBinding.inflate(layoutInflater)
        setContentView(accountViewBinding.root)

        accountManagerSetupUI()

        (application as VicinityApplication)
            .dependencyGraph
            .subDependencyGraph()
            .create(this@AccountInformation, accountViewBinding.rootView)
            .inject(this@AccountInformation)

        if (firebaseAuth.currentUser == null) {

            userInformation.startSignInProcess()

        } else {



        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        data?.let {

            when (requestCode) {
                UserInformation.GoogleSignInRequestCode -> {

                    val googleSignInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data)
                    val googleSignInAccount = googleSignInAccountTask.getResult(ApiException::class.java)

                    val authCredential = GoogleAuthProvider.getCredential(googleSignInAccount?.idToken, null)
                    firebaseAuth.signInWithCredential(authCredential).addOnSuccessListener {

                        val firebaseUser = firebaseAuth.currentUser

                        if (firebaseUser != null) {

                            val accountName: String = firebaseUser.email.toString()

                            userInformationIO.saveUserInformation(accountName)

                            startActivity(Intent(applicationContext, MapsOfSociety::class.java),
                                ActivityOptions.makeCustomAnimation(applicationContext, R.anim.slide_in_right, 0).toBundle())

                        }

                    }.addOnFailureListener {


                    }

                }
                else -> {

                }
            }

        }

    }

}