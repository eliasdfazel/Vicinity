/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 9/19/20 10:21 AM
 * Last modified 9/19/20 10:21 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.MapConfiguration.Map.InformationWindow

import android.view.View
import com.google.android.gms.maps.model.Marker
import net.geeksempire.vicinity.android.AccountManager.DataStructure.UserInformationDataStructure
import net.geeksempire.vicinity.android.MapConfiguration.Map.MapsOfSociety
import net.geeksempire.vicinity.android.R
import net.geeksempire.vicinity.android.Utils.UI.Theme.ThemeType
import net.geeksempire.vicinity.android.databinding.GoogleMapInformationWindowBinding

class InformationWindowUI (private val context: MapsOfSociety) {

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

            googleMapInformationWindowBinding.rootView.setOnClickListener {

                context.mapsViewBinding.informationWindowContainer.visibility = View.GONE

                context.mapsViewBinding.informationWindowContainer.removeAllViews()

            }

            googleMapInformationWindowBinding.userDisplayName.setOnClickListener {



            }

        }

    }

    fun commit() : View {

        return googleMapInformationWindowBinding.root
    }

}