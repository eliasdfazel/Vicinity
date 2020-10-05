/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 10/5/20 8:58 AM
 * Last modified 10/5/20 8:48 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.CommunicationConfiguration.Public.DataStructure

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

    var publicCommunityStorageImagesItemEndpoint: String? = null

    @ServerTimestamp var userMessageDate: Timestamp? = null
    @ServerTimestamp var userMessageEditDate: Timestamp? = null

    constructor() {/*  */}

    constructor(userIdentifier: String, userProfileImage: String, userDisplayName: String, userMessageTextContent: String, userMessageImageContent: String,
                publicCommunityStorageImagesItemEndpoint: String,
                userMessageDate: Timestamp, userMessageEditDate: Timestamp) {

        this@PublicMessageData.userIdentifier = userIdentifier
        this@PublicMessageData.userProfileImage = userProfileImage
        this@PublicMessageData.userDisplayName = userDisplayName

        this@PublicMessageData.userMessageTextContent = userMessageTextContent
        this@PublicMessageData.userMessageImageContent = userMessageImageContent

        this@PublicMessageData.publicCommunityStorageImagesItemEndpoint = publicCommunityStorageImagesItemEndpoint

        this@PublicMessageData.userMessageDate = userMessageDate
        this@PublicMessageData.userMessageEditDate = userMessageEditDate

    }



}