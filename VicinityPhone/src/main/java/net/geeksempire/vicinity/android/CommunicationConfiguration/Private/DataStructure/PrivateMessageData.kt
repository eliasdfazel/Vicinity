/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 10/23/20 6:03 AM
 * Last modified 10/23/20 5:46 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.CommunicationConfiguration.Private.DataStructure

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp

@Keep
object Unknown {
    const val unknownUserDisplayName = "Unknown ⚠"
    const val unknownProfileImage = "https://i.imgur.com/Vj8gLtW.jpg"

}

@Keep
class PrivateMessageData {

    var userIdentifier: String? = null
    var userProfileImage: String? = null
    var userDisplayName: String? = null

    var userMessageTextContent: String? = null
    var userMessageImageContent: String? = null

    var privateMessengerStorageImagesItemEndpoint: String? = null

    @ServerTimestamp var userMessageDate: Timestamp? = null
    @ServerTimestamp var userMessageEditDate: Timestamp? = null

    @Keep
    constructor() {/*  */}

    @Keep
    constructor(userIdentifier: String, userProfileImage: String, userDisplayName: String, userMessageTextContent: String, userMessageImageContent: String,
                privateMessengerStorageImagesItemEndpoint: String,
                userMessageDate: Timestamp, userMessageEditDate: Timestamp) {

        this@PrivateMessageData.userIdentifier = userIdentifier
        this@PrivateMessageData.userProfileImage = userProfileImage
        this@PrivateMessageData.userDisplayName = userDisplayName

        this@PrivateMessageData.userMessageTextContent = userMessageTextContent
        this@PrivateMessageData.userMessageImageContent = userMessageImageContent

        this@PrivateMessageData.privateMessengerStorageImagesItemEndpoint = privateMessengerStorageImagesItemEndpoint

        this@PrivateMessageData.userMessageDate = userMessageDate
        this@PrivateMessageData.userMessageEditDate = userMessageEditDate

    }

}