/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 9/21/20 9:01 AM
 * Last modified 9/21/20 9:01 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.MapConfiguration.Map.InformationWindow

import android.view.View
import com.google.android.gms.maps.model.Marker
import net.geeksempire.vicinity.android.AccountManager.DataStructure.UserInformationDataStructure
import net.geeksempire.vicinity.android.CommunicationConfiguration.Private.PrivateMessengerUI.PrivateMessenger
import net.geeksempire.vicinity.android.CommunicationConfiguration.Private.Utils.privateMessengerName
import net.geeksempire.vicinity.android.MapConfiguration.Map.MapsOfSociety
import net.geeksempire.vicinity.android.R
import net.geeksempire.vicinity.android.Utils.UI.Theme.ThemeType
import net.geeksempire.vicinity.android.databinding.GoogleMapInformationWindowBinding

class InformationWindow (private val context: MapsOfSociety) {

    private val googleMapInformationWindowBinding =  GoogleMapInformationWindowBinding.inflate(context.layoutInflater)

    var informationWindowData: InformationWindowData? = null

    init {

        when (context.overallTheme.checkThemeLightDark()) {
            ThemeType.ThemeLight -> {

                googleMapInformationWindowBinding.rootView.setBackgroundColor(context.getColor(R.color.light_blurry_color))

            }
            ThemeType.ThemeDark -> {

                googleMapInformationWindowBinding.rootView.setBackgroundColor(context.getColor(R.color.dark_blurry_color))

            }
        }

    }

    fun setUpContentContents(marker: Marker) {

        informationWindowData?.let { informationWindowData ->

            googleMapInformationWindowBinding.userDisplayName.text = informationWindowData.userDocument[UserInformationDataStructure.userDisplayName].toString()

            if (informationWindowData.userDocument[UserInformationDataStructure.instagramAccount].toString().isNotEmpty()) {

                googleMapInformationWindowBinding.instagramAddressLayout.visibility = View.VISIBLE
                googleMapInformationWindowBinding.instagramLogo.visibility = View.VISIBLE

                googleMapInformationWindowBinding.instagramAddressLayout.hint = "${informationWindowData.userDocument[UserInformationDataStructure.userDisplayName]}'s Instagram"
                googleMapInformationWindowBinding.instagramAddressView.setText(informationWindowData.userDocument[UserInformationDataStructure.instagramAccount].toString())

            }

            if (informationWindowData.userDocument[UserInformationDataStructure.twitterAccount].toString().isNotEmpty()) {

                googleMapInformationWindowBinding.twitterAddressLayout.visibility = View.VISIBLE
                googleMapInformationWindowBinding.twitterLogo.visibility = View.VISIBLE

                googleMapInformationWindowBinding.twitterAddressLayout.hint = "${informationWindowData.userDocument[UserInformationDataStructure.userDisplayName]}'s Twitter"
                googleMapInformationWindowBinding.twitterAddressView.setText(informationWindowData.userDocument[UserInformationDataStructure.twitterAccount].toString())

            }

            if (informationWindowData.userDocument[UserInformationDataStructure.phoneNumber].toString().isNotEmpty()) {

                googleMapInformationWindowBinding.phoneNumberAddressLayout.visibility = View.VISIBLE
                googleMapInformationWindowBinding.phoneNumberLogo.visibility = View.VISIBLE

                googleMapInformationWindowBinding.phoneNumberAddressLayout.hint = "${informationWindowData.userDocument[UserInformationDataStructure.userDisplayName]}'s Phone"
                googleMapInformationWindowBinding.phoneNumberAddressView.setText(informationWindowData.userDocument[UserInformationDataStructure.phoneNumber].toString().plus(if (informationWindowData.userDocument[UserInformationDataStructure.phoneNumberVerified].toString().toBoolean()) { " ✔" } else { "" }))

            }

            googleMapInformationWindowBinding.rootView.setOnClickListener {

                context.mapsViewBinding.informationWindowContainer.visibility = View.GONE

                context.mapsViewBinding.informationWindowContainer.removeAllViews()

            }

            if (context.firebaseUser!!.uid != informationWindowData.userDocument[UserInformationDataStructure.userIdentification].toString()) {

                googleMapInformationWindowBinding.enterPrivateChat.visibility = View.VISIBLE

                googleMapInformationWindowBinding.enterPrivateChat.playAnimation()

            } else {

                googleMapInformationWindowBinding.enterPrivateChat.visibility = View.INVISIBLE

            }

            googleMapInformationWindowBinding.enterPrivateChat.setOnClickListener {

                val selfUid = context.firebaseUser!!.uid
                val otherUid = "${informationWindowData.userDocument[UserInformationDataStructure.userIdentification]}"

                val privateMessengerName = privateMessengerName(selfUid, otherUid)

                PrivateMessenger.open(
                    context,
                    privateMessengerName
                )

            }

        }

    }

    fun getRootView() : GoogleMapInformationWindowBinding {

        return googleMapInformationWindowBinding
    }

}