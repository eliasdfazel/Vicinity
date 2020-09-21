/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 9/21/20 9:01 AM
 * Last modified 9/21/20 8:30 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.CommunicationConfiguration.Private.Utils

fun privateMessengerName(selfUid: String, otherUid: String) : String {

    val uidArray = arrayListOf<String>(selfUid, otherUid)

    uidArray.sort()

    return "${uidArray[0]}|${uidArray[1]}"
}