/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 11/6/20 9:07 AM
 * Last modified 11/6/20 9:01 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.Invitation.Receive

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import net.geeksempire.vicinity.android.CommunicationConfiguration.Private.PrivateMessengerUI.PrivateMessenger
import net.geeksempire.vicinity.android.CommunicationConfiguration.Private.Utils.privateMessengerName
import net.geeksempire.vicinity.android.Invitation.Utils.InvitationConstant

class ReceiveInvitationData : AppCompatActivity() {

    val firebaseUser: FirebaseUser? = Firebase.auth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Firebase.dynamicLinks
            .getDynamicLink(intent)
            .addOnSuccessListener(this@ReceiveInvitationData) { pendingDynamicLinkData ->

                pendingDynamicLinkData?.let {

                    pendingDynamicLinkData.link?.also { dynamicLinkUri ->

                        if (firebaseUser != null) {

                            val invitationType = dynamicLinkUri.getQueryParameter(InvitationConstant.InvitationType)!!

                            val uniqueUserId = dynamicLinkUri.getQueryParameter(InvitationConstant.UniqueUserId)!!
                            val userDisplayName = dynamicLinkUri.getQueryParameter(InvitationConstant.UserDisplayName)!!
                            val userProfileImage = dynamicLinkUri.getQueryParameter(InvitationConstant.UserProfileImage)!!

                            val privateMessengerName = privateMessengerName(uniqueUserId, firebaseUser.uid)

                            when (invitationType) {
                                InvitationConstant.InvitationTypes.Business -> {

                                    PrivateMessenger.open(
                                        context = applicationContext,
                                        privateMessengerName = privateMessengerName,
                                        otherUid = uniqueUserId,
                                        otherUsername = userDisplayName,
                                        otherProfileImage = userProfileImage
                                    )

                                }
                                InvitationConstant.InvitationTypes.Personal -> {



                                }
                            }

                        } else {

                            this@ReceiveInvitationData.finish()

                        }

                    }

                }

            }
            .addOnFailureListener(this) { exception ->
                exception.printStackTrace()

            }

    }

}