/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 9/20/20 4:45 AM
 * Last modified 9/20/20 4:45 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.AccountManager.UI

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import net.geeksempire.vicinity.android.AccountManager.Extensions.accountManagerSetupUI
import net.geeksempire.vicinity.android.AccountManager.Extensions.createUserProfile
import net.geeksempire.vicinity.android.AccountManager.Utils.UserInformation
import net.geeksempire.vicinity.android.AccountManager.Utils.UserInformationIO
import net.geeksempire.vicinity.android.MapConfiguration.Map.MapsOfSociety
import net.geeksempire.vicinity.android.R
import net.geeksempire.vicinity.android.Utils.Location.LocationCheckpoint
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

    val userInformation: UserInformation by lazy {
        UserInformation(this@AccountInformation)
    }

    val userInformationIO: UserInformationIO by lazy {
        UserInformationIO(applicationContext)
    }

    val locationCheckpoint = LocationCheckpoint()

    val firebaseAuth = Firebase.auth

    val firestoreDatabase: FirebaseFirestore = Firebase.firestore

    var profileUpdate: Boolean = false

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

            accountViewBinding.signupLoadingView.visibility = View.VISIBLE
            accountViewBinding.signupLoadingView.playAnimation()

            userInformation.startSignInProcess()

        } else {

            firebaseAuth.currentUser?.let { firebaseUser ->

                firestoreDatabase
                    .document(UserInformation.userProfileDatabasePath(firebaseUser.uid))
                    .get()
                    .addOnSuccessListener { documentSnapshot ->

                        documentSnapshot?.let { documentData ->

                            accountViewBinding.socialMediaScrollView.startAnimation(AnimationUtils.loadAnimation(applicationContext, R.anim.fade_in))
                            accountViewBinding.socialMediaScrollView.visibility = View.VISIBLE

                            accountViewBinding.instagramAddressView.setText(documentData.data?.get("instagramAccount").toString())

                            accountViewBinding.twitterAddressView.setText(documentData.data?.get("twitterAccount").toString())

                            accountViewBinding.phoneNumberAddressView.setText(documentData.data?.get("phoneNumber").toString())

                        }

                    }

            }

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

                            createUserProfile()

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

    override fun onBackPressed() {

        if (profileUpdate) {

            profileUpdate = false

            this@AccountInformation.finish()

        }

    }

}