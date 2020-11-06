/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 11/6/20 7:11 AM
 * Last modified 11/6/20 7:11 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.Invitation.Receive

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import net.geeksempire.vicinity.android.Invitation.Utils.InvitationConstant

class ReceiveInvitationData : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Firebase.dynamicLinks
            .getDynamicLink(intent)
            .addOnSuccessListener(this@ReceiveInvitationData) { pendingDynamicLinkData ->

                pendingDynamicLinkData?.let {

                    pendingDynamicLinkData.link?.also { dynamicLinkUri ->

                        val uniqueUserId = dynamicLinkUri.getQueryParameter(InvitationConstant.UniqueUserId)

                    }

                }

            }
            .addOnFailureListener(this) { exception ->
                exception.printStackTrace()

            }

    }

}