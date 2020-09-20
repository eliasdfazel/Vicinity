/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 9/20/20 9:10 AM
 * Last modified 9/20/20 8:43 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.CommunicationConfiguration.Private.Utils

fun privateMessengerName(selfUid: String, otherUid: String) : String {

    return "${selfUid}|${otherUid}"
}

fun reversePrivateMessengerName(selfUid: String, otherUid: String) : String {

    return "${otherUid}|${selfUid}"
}