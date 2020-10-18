/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 10/18/20 4:03 AM
 * Last modified 10/18/20 3:53 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.CommunicationConfiguration.Private.DataStructure

import com.google.firebase.firestore.FieldValue

data class PrivateMessengerData (var PersonOne: String,
                                 var PersonOneUsername: String,
                                 var PersonTwo: String,
                                 var PersonTwoUsername: String,
                                 var Date: FieldValue = FieldValue.serverTimestamp())