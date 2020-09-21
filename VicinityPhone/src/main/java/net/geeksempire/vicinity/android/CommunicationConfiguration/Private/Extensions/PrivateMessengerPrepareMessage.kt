/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 9/21/20 10:29 AM
 * Last modified 9/21/20 10:27 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.CommunicationConfiguration.Private.Extensions

import com.google.firebase.firestore.FieldValue
import net.geeksempire.vicinity.android.CommunicationConfiguration.Private.PrivateMessengerUI.PrivateMessenger
import net.geeksempire.vicinity.android.CommunicationConfiguration.Public.DataStructure.Unknown
import net.geeksempire.vicinity.android.R

fun PrivateMessenger.privateMessengerPrepareMessage() : LinkedHashMap<String, Any> {

    val publicMessageDataItem: LinkedHashMap<String, Any> = LinkedHashMap<String, Any>()

    publicMessageDataItem["userIdentifier"] = firebaseUser.uid
    publicMessageDataItem["userProfileImage"] = firebaseUser.photoUrl.toString()?: Unknown.unknownProfileImage
    publicMessageDataItem["userDisplayName"] = firebaseUser.displayName?: Unknown.unknownUserDisplayName
    publicMessageDataItem["userMessageTextContent"] = privateMessengerViewBinding.textMessageContentView.text.toString()

    publicMessageDataItem["userMessageDate"] = FieldValue.serverTimestamp()

    return publicMessageDataItem
}

fun PrivateMessenger.privateMessengerPrepareNotificationData(messageContent: String, privateMessengerName: String) : LinkedHashMap<String, Any> {

    val publicMessageDataItem: LinkedHashMap<String, Any> = LinkedHashMap<String, Any>()

    publicMessageDataItem["notificationTopic"] = privateMessengerPrepareNotificationTopic(privateMessengerName)
    publicMessageDataItem["selfUid"] = firebaseUser.uid
    publicMessageDataItem["selfDisplayName"] = firebaseUser.displayName.toString()
    publicMessageDataItem["privateMessengerAction"] = getString(R.string.privateMessengerAction)
    publicMessageDataItem["privateMessengerName"] = privateMessengerName
    publicMessageDataItem["notificationLargeIcon"] = firebaseUser.photoUrl.toString()
    publicMessageDataItem["messageContent"] = messageContent

    return publicMessageDataItem
}

fun PrivateMessenger.privateMessengerPrepareNotificationTopic(privateMessengerName: String) : String {

    return privateMessengerName.replace("|", "")
}