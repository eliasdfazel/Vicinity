/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 10/5/20 8:58 AM
 * Last modified 10/5/20 8:46 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.CommunicationConfiguration.Public.Endpoint

import com.google.android.gms.maps.model.LatLng
import net.geeksempire.vicinity.android.MapConfiguration.Vicinity.vicinityName

class PublicCommunicationEndpoint {

    companion object {

        var CurrentCommunityCoordinates: LatLng? = null

        /**
         * Collection Path: Odd
         * Document Path : Even
         **/
        private const val commonPublicEndpoint: String = "Vicinity/OnlineSociety/Public/Community/"

        fun publicCommunityDocumentEndpoint(countryName: String, locationLatitudeLongitude: LatLng) : String {

            return PublicCommunicationEndpoint.commonPublicEndpoint + countryName + "/" + vicinityName(locationLatitudeLongitude)
        }

        fun publicCommunityCollectionEndpoint(countryName: String) : String {

            return PublicCommunicationEndpoint.commonPublicEndpoint + countryName
        }

        fun publicCommunityStorageImagesItemEndpoint(publicCommunityMessagesDatabasePath: String, documentSnapshotId: String) : String {

            return "$publicCommunityMessagesDatabasePath/$documentSnapshotId"
        }

        fun publicCommunityStorageImagesItemEndpoint(publicCommunityMessagesDatabasePath: String, documentSnapshotId: String, imageIndex: String) : String {

            return publicCommunityMessagesDatabasePath + "/" + documentSnapshotId + "/" + "Image${imageIndex}.JPEG"
        }

        fun publicCommunityStoragePreviewImageEndpoint(publicCommunityMessagesDatabasePath: String, documentSnapshotId: String) : String {

            return publicCommunityMessagesDatabasePath + "/" + documentSnapshotId + "/" + "PreviewImage.JPEG"
        }

    }

}