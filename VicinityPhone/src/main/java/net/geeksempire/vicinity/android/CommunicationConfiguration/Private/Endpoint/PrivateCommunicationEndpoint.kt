/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 10/8/20 7:09 AM
 * Last modified 10/8/20 7:09 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.CommunicationConfiguration.Private.Endpoint

class PrivateCommunicationEndpoint {

    companion object {

        /**
         * Collection Path: Odd
         * Document Path : Even
         **/
        private const val commonPrivateEndpoint: String = "Vicinity/OnlineSociety/PrivateMessenger/"

        fun privateMessengerDocumentEndpoint(privateMessengerName: String) : String {

            return PrivateCommunicationEndpoint.commonPrivateEndpoint + privateMessengerName
        }

        fun privateMessengerCollectionEndpoint(privateMessengerName: String) : String {

            return PrivateCommunicationEndpoint.commonPrivateEndpoint + privateMessengerName
        }

        fun privateMessengerStorageImagesItemEndpoint(privateMessengerMessagesDatabasePath: String, documentSnapshotId: String) : String {

            return "$privateMessengerMessagesDatabasePath/$documentSnapshotId"
        }

        fun privateMessengerStorageImagesItemEndpoint(privateMessengerMessagesDatabasePath: String, documentSnapshotId: String, imageIndex: String) : String {

            return privateMessengerMessagesDatabasePath + "/" + documentSnapshotId + "/" + "Image${imageIndex}.JPEG"
        }

        fun privateMessengerStoragePreviewImageEndpoint(privateMessengerMessagesDatabasePath: String, documentSnapshotId: String) : String {

            return privateMessengerMessagesDatabasePath + "/" + documentSnapshotId + "/" + "PreviewImage.JPEG"
        }

    }
}