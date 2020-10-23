/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 10/23/20 6:03 AM
 * Last modified 10/23/20 5:48 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.CommunicationConfiguration.Private.DataStructure

import androidx.annotation.Keep
import com.google.firebase.firestore.FieldValue

@Keep
object PrivateMessengerUsersDataStructure {
    const val personOne: String = "personOne"
    const val personOneUsername: String = "personOneUsername"
    const val personOneProfileImage: String = "personOneProfileImage"
    const val personTwo: String = "personTwo"
    const val personTwoUsername: String = "personTwoUsername"
    const val personTwoProfileImage: String = "personTwoProfileImage"
}

@Keep
data class PrivateMessengerData (var PersonOne: String,
                                 var PersonOneUsername: String,
                                 var PersonOneProfileImage: String,
                                 var PersonTwo: String,
                                 var PersonTwoUsername: String,
                                 var PersonTwoProfileImage: String,
                                 var Date: FieldValue = FieldValue.serverTimestamp())