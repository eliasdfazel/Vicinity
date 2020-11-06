/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 11/6/20 7:11 AM
 * Last modified 11/6/20 7:11 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.Invitation.Send

import android.content.Context
import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.dynamiclinks.ktx.*
import com.google.firebase.ktx.Firebase
import net.geeksempire.vicinity.android.Invitation.Utils.InvitationConstant

class BusinessInvitation (val context: Context) {

    fun invite(firebaseUser: FirebaseUser) {

        val dynamicLink = Firebase.dynamicLinks.dynamicLink {

            link = Uri.parse("https://www.geeksempire.net/")
                .buildUpon()
                .appendQueryParameter(InvitationConstant.UniqueUserId, firebaseUser.uid).build()

            domainUriPrefix = "https://geeksempire.page.link"

            socialMetaTagParameters {

                title = firebaseUser.uid

            }

            androidParameters(context.packageName) {

            }

            iosParameters(context.packageName) {

            }

        }

        val dynamicLinkUri = dynamicLink.uri



    }

}