/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 9/21/20 11:03 AM
 * Last modified 9/21/20 10:58 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.CommunicationConfiguration.Private.DataStructure

import com.google.firebase.firestore.FieldValue

data class PrivateMessengerData (var PersonOne: String, var PersonTwo: String,
                                 var Date: FieldValue = FieldValue.serverTimestamp())