/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 12/7/20 7:41 AM
 * Last modified 12/7/20 7:38 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.MapConfiguration.Vicinity.People.DataProcess

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import net.geeksempire.vicinity.android.AccountManager.DataStructure.PeopleData
import net.geeksempire.vicinity.android.AccountManager.DataStructure.UserInformationDataStructure
import net.geeksempire.vicinity.android.Utils.Calendar.formatToCurrentTimeZone

class PeopleDataProcess : ViewModel() {

    val userInformationProfiles: MutableLiveData<ArrayList<PeopleData>> by lazy {
        MutableLiveData<ArrayList<PeopleData>>()
    }

    fun preparePeopleData(documentSnapshots: List<DocumentSnapshot>) = CoroutineScope(Dispatchers.IO).async {

        val documentSnapshotsList = ArrayList<PeopleData>()

        documentSnapshots.forEachIndexed { index, documentSnapshot ->
            Log.d(this@PeopleDataProcess.javaClass.simpleName, documentSnapshot[UserInformationDataStructure.userEmailAddress].toString())

            documentSnapshotsList.add(PeopleData(
                userIdentification = documentSnapshot[UserInformationDataStructure.userIdentification].toString(),
                userDisplayName = documentSnapshot[UserInformationDataStructure.userDisplayName].toString(),
                userEmailAddress = documentSnapshot[UserInformationDataStructure.userEmailAddress].toString(),
                userProfileImage = documentSnapshot[UserInformationDataStructure.userProfileImage].toString(),
                userLatitude = documentSnapshot[UserInformationDataStructure.userLatitude].toString(),
                userLongitude = documentSnapshot[UserInformationDataStructure.userLongitude].toString(),
                userLastSignIn = documentSnapshot[UserInformationDataStructure.userLastSignIn].toString().formatToCurrentTimeZone().toString(),
                userState = documentSnapshot[UserInformationDataStructure.userLongitude].toString()
            ))

        }

        userInformationProfiles.postValue(documentSnapshotsList)

    }

}