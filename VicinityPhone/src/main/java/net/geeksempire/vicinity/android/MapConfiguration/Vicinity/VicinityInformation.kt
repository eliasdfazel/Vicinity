/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 10/18/20 9:41 AM
 * Last modified 10/18/20 9:31 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.MapConfiguration.Vicinity

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import net.geeksempire.vicinity.android.R
import net.geeksempire.vicinity.android.Utils.Location.LocationCheckpoint
import net.geeksempire.vicinity.android.Utils.Preferences.ReadPreferences
import net.geeksempire.vicinity.android.Utils.Preferences.SavePreferences

interface CountryInformationInterface {
    fun countryNameReady(nameOfCountry: String)
}

class VicinityInformation (private val context: Context) {

    object CountriesName {
//const val AD
//const val AE
//const val AF
//const val AG
//const val AI
//const val AL
//const val AM
//const val AN
//const val AO
//const val AQ
//const val AR
//const val AS
//const val AT
//const val AU
//const val AW
//const val AZ
//const val BA
//const val BB
//const val BD
//const val BE
//const val BF
//const val BG
//const val BH
//const val BI
//const val BJ
//const val BM
//const val BN
//const val BO
//const val BR
//const val BS
//const val BT
//const val BV
//const val BW
//const val BY
//const val BZ
//const val CA
//const val CC
//const val CD
//const val CF
//const val CG
//const val CH
//const val CI
//const val CK
//const val CL
//const val CM
//const val CN
//const val CO
//const val CR
//const val CU
//const val CV
//const val CX
//const val CY
//const val CZ
//const val DE
//const val DJ
//const val DK
//const val DM
//const val DO
//const val DZ
//const val EC
//const val EE
//const val EG
//const val EH
//const val ER
//const val ES
//const val ET
//const val FI
//const val FJ
//const val FK
//const val FM
//const val FO
//const val FR
//const val GA
//const val GB
//const val GD
//const val GE
//const val GF
//const val GG
//const val GH
//const val GI
//const val GL
//const val GM
//const val GN
//const val GP
//const val GQ
//const val GR
//const val GS
//const val GT
//const val GU
//const val GW
//const val GY
//const val GZ
//const val HK
//const val HM
//const val HN
//const val HR
//const val HT
//const val HU
//const val ID
//const val IE
//const val IL
//const val IM
//const val IN
//const val IO
//const val IQ
//const val IR
//const val IS
//const val IT
//const val JE
//const val JM
//const val JO
//const val JP
//const val KE
//const val KG
//const val KH
//const val KI
//const val KM
//const val KN
//const val KP
//const val KR
//const val KW
//const val KY
//const val KZ
//const val LA
//const val LB
//const val LC
//const val LI
//const val LK
//const val LR
//const val LS
//const val LT
//const val LU
//const val LV
//const val LY
//const val MA
//const val MC
//const val MD
//const val ME
//const val MG
//const val MH
//const val MK
//const val ML
//const val MM
//const val MN
//const val MO
//const val MP
//const val MQ
//const val MR
//const val MS
//const val MT
//const val MU
//const val MV
//const val MW
//const val MX
//const val MY
//const val MZ
//const val NA
//const val NC
//const val NE
//const val NF
//const val NG
//const val NI
//const val NL
//const val NO
//const val NP
//const val NR
//const val NU
//const val NZ
//const val OM
//const val PA
//const val PE
//const val PF
//const val PG
//const val PH
//const val PK
//const val PL
//const val PM
//const val PN
//const val PR
//const val PS
//const val PT
//const val PW
//const val PY
//const val QA
//const val RE
//const val RO
//const val RS
//const val RU
//const val RW
//const val SA
//const val SB
//const val SC
//const val SD
//const val SE
//const val SG
//const val SH
//const val SI
//const val SJ
//const val SK
//const val SL
//const val SM
//const val SN
//const val SO
//const val SR
//const val ST
//const val SV
//const val SY
//const val SZ
//const val TC
//const val TD
//const val TF
//const val TG
//const val TH
//const val TJ
//const val TK
//const val TL
//const val TM
//const val TN
//const val TO
//const val TR
//const val TT
//const val TV
//const val TW
//const val TZ
//const val UA
//const val UG
//const val UK
//const val UM
        const val US = "US"
        const val UnitedStates = "United States"
//const val UY
//const val UZ
//const val VA
//const val VC
//const val VE
//const val VG
//const val VI
//const val VN
//const val VU
//const val WF
//const val WS
//const val XK
//const val YE
//const val YT
//const val ZA
//const val ZM
//const val ZW
    }

    private val savePreferences = SavePreferences(context)

    private val readPreferences: ReadPreferences = ReadPreferences(context)

    fun getCurrentCountryName(countryIso: String, countryInformationInterface: CountryInformationInterface) {

        val firebaseDatabaseCountriesLocation: FirebaseDatabase = Firebase.database("https://vicinity-online-society-countries-information.firebaseio.com/")

        val databaseReferenceCountriesLocation = firebaseDatabaseCountriesLocation.reference
            .child("CountriesInformation")
            .child(countryIso)

        databaseReferenceCountriesLocation.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d(this@VicinityInformation.javaClass.simpleName, dataSnapshot.child("Country").value.toString())

                savePreferences.savePreference("UserInformation", "Country", dataSnapshot.child("Country").value.toString())
                savePreferences.savePreference("UserInformation", "CountryCode", dataSnapshot.child("PhoneCode").value.toString())

                countryInformationInterface.countryNameReady(dataSnapshot.child("Country").value.toString())

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }

        })

    }

    fun knownLocationName(location: LatLng) : String? {

        return readPreferences.readPreference("VicinityInformation", vicinityName(location), LocationCheckpoint.LOCATION_INFORMATION_DETAIL)
    }

    fun loadCountryFlag(countryISO: String?) : Drawable? {

        return when (countryISO) {
            VicinityInformation.CountriesName.US, VicinityInformation.CountriesName.UnitedStates -> {
                context.getDrawable(R.drawable.ic_usd_flag)
            }
            else -> {
                null
            }
        }

    }

}