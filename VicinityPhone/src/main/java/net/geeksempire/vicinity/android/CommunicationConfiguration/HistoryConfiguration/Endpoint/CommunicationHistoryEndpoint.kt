/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 10/17/20 6:37 AM
 * Last modified 10/17/20 6:17 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.CommunicationConfiguration.HistoryConfiguration.Endpoint

class CommunicationHistoryEndpoint {

    companion object {

        fun publicVicinityArchiveDatabasePath(userUniqueIdentifier: String, vicinityName: String) : String = "Vicinity/UserInformation/${userUniqueIdentifier}/History/PublicCommunity/"

        fun privateVicinityArchiveDatabasePath(userUniqueIdentifier: String, vicinityName: String) : String = "Vicinity/UserInformation/${userUniqueIdentifier}/History/PrivateMessenger/"
    }

}