/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 9/5/20 11:05 AM
 * Last modified 9/5/20 11:01 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.CommunicationConfiguration.Public.Endpoint

import com.google.android.gms.maps.model.LatLng
import net.geeksempire.vicinity.android.MapConfiguration.Vicinity.vicinityName

class PublicCommunicationEndpoint {

    companion object {
        /*
         * Collection Path: Odd
         * Document Path : Even
         * */
        private const val commonPublicEndpoint: String = "Vicinity/OnlineSociety/Public/Community/"

        fun publicCommunityDocumentEndpoint(countryName: String, locationLatitudeLongitude: LatLng) : String {

            return PublicCommunicationEndpoint.commonPublicEndpoint + countryName + "/" + vicinityName(locationLatitudeLongitude)
        }

        fun publicCommunityCollectionEndpoint(countryName: String) : String {

            return PublicCommunicationEndpoint.commonPublicEndpoint + countryName
        }
    }

}