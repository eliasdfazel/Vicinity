/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 9/21/20 11:03 AM
 * Last modified 9/21/20 10:54 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.CommunicationConfiguration.Private.DataStructure

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp

object Unknown {
    const val unknownUserDisplayName = "Unknown ⚠"
    const val unknownProfileImage = "https://i.imgur.com/Vj8gLtW.jpg"

}

class PrivateMessageData {

    var userIdentifier: String? = null
    var userProfileImage: String? = null
    var userDisplayName: String? = null

    var userMessageTextContent: String? = null
    var userMessageImageContent: String? = null

    @ServerTimestamp var userMessageDate: Timestamp? = null
    @ServerTimestamp var userMessageEditDate: Timestamp? = null

    constructor() {/*  */}

    constructor(userIdentifier: String, userProfileImage: String, userDisplayName: String, userMessageTextContent: String, userMessageImageContent: String,
                userMessageDate: Timestamp, userMessageEditDate: Timestamp) {

        this@PrivateMessageData.userIdentifier = userIdentifier
        this@PrivateMessageData.userProfileImage = userProfileImage
        this@PrivateMessageData.userDisplayName = userDisplayName

        this@PrivateMessageData.userMessageTextContent = userMessageTextContent
        this@PrivateMessageData.userMessageImageContent = userMessageImageContent

        this@PrivateMessageData.userMessageDate = userMessageDate
        this@PrivateMessageData.userMessageEditDate = userMessageEditDate

    }

}