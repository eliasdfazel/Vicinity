/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 10/18/20 9:29 AM
 * Last modified 10/18/20 9:19 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.CommunicationConfiguration.Private.DataStructure

import com.google.firebase.firestore.FieldValue

object PrivateMessengerUsersDataStructure {
    const val personOne: String = "personOne"
    const val personOneUsername: String = "personOneUsername"
    const val personTwo: String = "personTwo"
    const val personTwoUsername: String = "personTwoUsername"
}

data class PrivateMessengerData (var PersonOne: String,
                                 var PersonOneUsername: String,
                                 var PersonOneProfileImage: String,
                                 var PersonTwo: String,
                                 var PersonTwoUsername: String,
                                 var PersonTwoProfileImage: String,
                                 var Date: FieldValue = FieldValue.serverTimestamp())