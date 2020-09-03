/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 9/3/20 8:48 AM
 * Last modified 9/3/20 7:35 AM
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
        private const val commonPublicEndpoint: String = "Vicinity/Public/Community/"

        fun publicCommunityEndpoint(countryName: String, locationLatitudeLongitude: LatLng) : String {

            return PublicCommunicationEndpoint.commonPublicEndpoint + countryName + "/" + vicinityName(locationLatitudeLongitude)
        }
    }

}