/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 9/20/20 9:10 AM
 * Last modified 9/20/20 8:44 AM
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

class PublicMessageData {

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

        this@PublicMessageData.userIdentifier = userIdentifier
        this@PublicMessageData.userProfileImage = userProfileImage
        this@PublicMessageData.userDisplayName = userDisplayName

        this@PublicMessageData.userMessageTextContent = userMessageTextContent
        this@PublicMessageData.userMessageImageContent = userMessageImageContent

        this@PublicMessageData.userMessageDate = userMessageDate
        this@PublicMessageData.userMessageEditDate = userMessageEditDate

    }



}