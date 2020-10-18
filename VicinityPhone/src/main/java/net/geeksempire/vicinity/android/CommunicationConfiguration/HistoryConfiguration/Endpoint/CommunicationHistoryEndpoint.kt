/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 10/18/20 5:18 AM
 * Last modified 10/18/20 4:26 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.CommunicationConfiguration.HistoryConfiguration.Endpoint

class CommunicationHistoryEndpoint {

    companion object {

        fun publicVicinityArchiveDatabasePath(userUniqueIdentifier: String) : String = "Vicinity/UserInformation/${userUniqueIdentifier}/History/PublicCommunity/"

        fun privateVicinityArchiveDatabasePath(userUniqueIdentifier: String) : String = "Vicinity/UserInformation/${userUniqueIdentifier}/History/PrivateMessenger/"
    }

}