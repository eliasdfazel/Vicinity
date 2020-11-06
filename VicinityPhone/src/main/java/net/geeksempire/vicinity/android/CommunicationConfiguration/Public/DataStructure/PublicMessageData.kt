/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 11/6/20 9:07 AM
 * Last modified 11/6/20 9:04 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.CommunicationConfiguration.Public.DataStructure

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp

@Keep
object Unknown {
    const val unknownUserDisplayName = "Unknown ⚠"
    const val unknownProfileImage = "https://i.imgur.com/Vj8gLtW.jpg"
}

@Keep
class PublicMessageData {

    var userIdentifier: String? = null
    var userProfileImage: String? = null
    var userDisplayName: String? = null

    var userMessageTextContent: String? = null
    var userMessageImageContent: String? = null

    var publicCommunityStorageImagesItemEndpoint: String? = null

    @ServerTimestamp var userMessageDate: Timestamp? = null
    @ServerTimestamp var userMessageEditDate: Timestamp? = null

    @Keep
    constructor() {/*  */}

    @Keep
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