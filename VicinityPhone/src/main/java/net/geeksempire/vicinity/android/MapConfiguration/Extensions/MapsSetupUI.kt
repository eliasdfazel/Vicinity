/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 12/7/20 6:02 AM
 * Last modified 12/7/20 6:02 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.MapConfiguration.Extensions

import android.app.ActivityOptions
import android.content.Intent
import androidx.constraintlayout.widget.ConstraintLayout
import net.geeksempire.vicinity.android.AccountManager.Utils.UserInformation
import net.geeksempire.vicinity.android.CommunicationConfiguration.Public.Endpoint.PublicCommunicationEndpoint
import net.geeksempire.vicinity.android.MapConfiguration.Map.MapsOfSociety
import net.geeksempire.vicinity.android.MapConfiguration.Vicinity.People.UI.ListOfPeople
import net.geeksempire.vicinity.android.Preferences.PreferencesControl
import net.geeksempire.vicinity.android.Utils.UI.Display.navigationBarHeight
import net.geeksempire.vicinity.android.Utils.UI.Display.statusBarHeight
import net.geeksempire.vicinity.android.Utils.UI.Theme.ThemeType
import kotlin.math.roundToInt

fun MapsOfSociety.mapsOfSocietySetupUI() {

    when (overallTheme.checkThemeLightDark()) {
        ThemeType.ThemeLight -> {

        }
        ThemeType.ThemeDark -> {

        }
    }

    val preferenceViewLayoutParams = mapsViewBinding.preferenceView.layoutParams as ConstraintLayout.LayoutParams
    preferenceViewLayoutParams.setMargins(0, preferenceViewLayoutParams.topMargin + statusBarHeight(applicationContext), 0, 0)
    mapsViewBinding.preferenceView.layoutParams = preferenceViewLayoutParams

    val communicationHistoryLayoutParams = mapsViewBinding.communicationHistory.layoutParams as ConstraintLayout.LayoutParams
    communicationHistoryLayoutParams.setMargins(0, communicationHistoryLayoutParams.topMargin + statusBarHeight(applicationContext), 0, 0)
    mapsViewBinding.communicationHistory.layoutParams = communicationHistoryLayoutParams

    val showPeopleLayoutParams = mapsViewBinding.showPeopleView.layoutParams as ConstraintLayout.LayoutParams
    showPeopleLayoutParams.setMargins(0, 0, 0, showPeopleLayoutParams.bottomMargin + navigationBarHeight(applicationContext))
    mapsViewBinding.showPeopleView.layoutParams = showPeopleLayoutParams

    clicksSetup()

}

fun MapsOfSociety.clicksSetup() {

    mapsViewBinding.preferenceView.setOnClickListener {

        startActivity(Intent(applicationContext, PreferencesControl::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }, ActivityOptions.makeScaleUpAnimation(mapsViewBinding.preferenceView, mapsViewBinding.preferenceView.x.roundToInt(), mapsViewBinding.preferenceView.y.roundToInt(), mapsViewBinding.preferenceView.width, mapsViewBinding.preferenceView.height).toBundle())

    }

    mapsViewBinding.showPeopleView.setOnClickListener {

        ListOfPeople.openListOfPeople(
            this@clicksSetup,
            UserInformation.allUsersInformationDatabasePath(PublicCommunicationEndpoint.publicCommunityDocumentEndpoint(nameOfCountry!!, PublicCommunicationEndpoint.CurrentCommunityCoordinates!!))
        )

    }

}