/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 9/15/20 10:09 AM
 * Last modified 9/15/20 9:00 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.AccountManager.DataStructure

import com.google.firebase.firestore.FieldValue

data class UserInformationData (var userIdentification: String, var userEmailAddress: String, var userDisplayName: String, var userProfileImage: String,
                                var userLatitude: String, var userLongitude: String,
                                var userState: String,
                                var userLastSignIn:  FieldValue,
                                var userJointDate:  FieldValue = FieldValue.serverTimestamp())

data class UserInformationProfileData (var userIdentification: String, var userEmailAddress: String, var userDisplayName: String, var userProfileImage: String,
                                       var userLatitude: String, var userLongitude: String,
                                       var instagramAccount: String?,
                                       var twitterAccount: String?,
                                       var phoneNumber: String?,
                                       var userJointDate:  FieldValue = FieldValue.serverTimestamp())