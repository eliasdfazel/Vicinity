/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 9/12/20 5:17 AM
 * Last modified 9/12/20 5:00 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.CommunicationConfiguration.Public.Extensions

import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FieldValue
import net.geeksempire.vicinity.android.CommunicationConfiguration.Public.DataStructure.Unknown
import net.geeksempire.vicinity.android.CommunicationConfiguration.Public.PublicCommunityUI.PublicCommunity
import net.geeksempire.vicinity.android.R

fun PublicCommunity.publicCommunityPrepareMessage() : LinkedHashMap<String, Any> {

    val publicMessageDataItem: LinkedHashMap<String, Any> = LinkedHashMap<String, Any>()

    publicMessageDataItem["userIdentifier"] = firebaseUser.uid
    publicMessageDataItem["userProfileImage"] = firebaseUser.photoUrl.toString()?:Unknown.unknownProfileImage
    publicMessageDataItem["userDisplayName"] = firebaseUser.displayName?:Unknown.unknownUserDisplayName
    publicMessageDataItem["userMessageTextContent"] = publicCommunityViewBinding.textMessageContentView.text.toString()

    publicMessageDataItem["userMessageDate"] = FieldValue.serverTimestamp()

    return publicMessageDataItem
}

fun PublicCommunity.publicCommunityPrepareNotificationData(messageContent: String, publicCommunityName: String, publicCommunityCountryName: String,communityCenterVicinity: LatLng) : LinkedHashMap<String, Any> {

    val publicMessageDataItem: LinkedHashMap<String, Any> = LinkedHashMap<String, Any>()

    publicMessageDataItem["notificationTopic"] = publicCommunityPrepareNotificationTopic(publicCommunityName)
    publicMessageDataItem["selfUid"] = firebaseUser.uid
    publicMessageDataItem["selfDisplayName"] = firebaseUser.displayName.toString()
    publicMessageDataItem["publicCommunityAction"] = getString(R.string.publicCommunityAction)
    publicMessageDataItem["nameOfCountry"] = publicCommunityCountryName
    publicMessageDataItem["vicinityLatitude"] = communityCenterVicinity.latitude.toString()
    publicMessageDataItem["vicinityLongitude"] = communityCenterVicinity.longitude.toString()
    publicMessageDataItem["publicCommunityName"] = publicCommunityName
    publicMessageDataItem["notificationLargeIcon"] = firebaseUser.photoUrl.toString()
    publicMessageDataItem["messageContent"] = messageContent

    return publicMessageDataItem
}

fun PublicCommunity.publicCommunityPrepareNotificationTopic(publicCommunityName: String) : String {

    return publicCommunityName.replace("|", "")
}