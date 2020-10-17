/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 10/17/20 6:37 AM
 * Last modified 10/17/20 6:37 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.CommunicationConfiguration.HistoryConfiguration.DataStructure

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.geeksempire.vicinity.android.AccountManager.DataStructure.UserInformationVicinityArchiveData
import net.geeksempire.vicinity.android.CommunicationConfiguration.Private.DataStructure.PrivateMessengerData

class HistoryLiveData : ViewModel() {

    val publicCommunicationHistory: MutableLiveData<ArrayList<UserInformationVicinityArchiveData>> by lazy {
        MutableLiveData<ArrayList<UserInformationVicinityArchiveData>>()
    }

    val privateCommunicationHistory: MutableLiveData<ArrayList<PrivateMessengerData>> by lazy {
        MutableLiveData<ArrayList<PrivateMessengerData>>()
    }

}