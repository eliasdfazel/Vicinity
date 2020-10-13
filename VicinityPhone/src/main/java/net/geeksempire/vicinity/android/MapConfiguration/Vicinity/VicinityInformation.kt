/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 10/13/20 6:24 AM
 * Last modified 10/13/20 6:24 AM
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
//        AD
//        AE
//        AF
//        AG
//        AI
//        AL
//        AM
//        AN
//        AO
//        AQ
//        AR
//        AS
//        AT
//        AU
//        AW
//        AZ
//        BA
//        BB
//        BD
//        BE
//        BF
//        BG
//        BH
//        BI
//        BJ
//        BM
//        BN
//        BO
//        BR
//        BS
//        BT
//        BV
//        BW
//        BY
//        BZ
//        CA
//        CC
//        CD
//        CF
//        CG
//        CH
//        CI
//        CK
//        CL
//        CM
//        CN
//        CO
//        CR
//        CU
//        CV
//        CX
//        CY
//        CZ
//        DE
//        DJ
//        DK
//        DM
//        DO
//        DZ
//        EC
//        EE
//        EG
//        EH
//        ER
//        ES
//        ET
//        FI
//        FJ
//        FK
//        FM
//        FO
//        FR
//        GA
//        GB
//        GD
//        GE
//        GF
//        GG
//        GH
//        GI
//        GL
//        GM
//        GN
//        GP
//        GQ
//        GR
//        GS
//        GT
//        GU
//        GW
//        GY
//        GZ
//        HK
//        HM
//        HN
//        HR
//        HT
//        HU
//        ID
//        IE
//        IL
//        IM
//        IN
//        IO
//        IQ
//        IR
//        IS
//        IT
//        JE
//        JM
//        JO
//        JP
//        KE
//        KG
//        KH
//        KI
//        KM
//        KN
//        KP
//        KR
//        KW
//        KY
//        KZ
//        LA
//        LB
//        LC
//        LI
//        LK
//        LR
//        LS
//        LT
//        LU
//        LV
//        LY
//        MA
//        MC
//        MD
//        ME
//        MG
//        MH
//        MK
//        ML
//        MM
//        MN
//        MO
//        MP
//        MQ
//        MR
//        MS
//        MT
//        MU
//        MV
//        MW
//        MX
//        MY
//        MZ
//        NA
//        NC
//        NE
//        NF
//        NG
//        NI
//        NL
//        NO
//        NP
//        NR
//        NU
//        NZ
//        OM
//        PA
//        PE
//        PF
//        PG
//        PH
//        PK
//        PL
//        PM
//        PN
//        PR
//        PS
//        PT
//        PW
//        PY
//        QA
//        RE
//        RO
//        RS
//        RU
//        RW
//        SA
//        SB
//        SC
//        SD
//        SE
//        SG
//        SH
//        SI
//        SJ
//        SK
//        SL
//        SM
//        SN
//        SO
//        SR
//        ST
//        SV
//        SY
//        SZ
//        TC
//        TD
//        TF
//        TG
//        TH
//        TJ
//        TK
//        TL
//        TM
//        TN
//        TO
//        TR
//        TT
//        TV
//        TW
//        TZ
//        UA
//        UG
//        UK
//        UM
        const val US = "US"
//        UY
//        UZ
//        VA
//        VC
//        VE
//        VG
//        VI
//        VN
//        VU
//        WF
//        WS
//        XK
//        YE
//        YT
//        ZA
//        ZM
//        ZW
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
            VicinityInformation.CountriesName.US -> {
                context.getDrawable(R.drawable.ic_usd_flag)
            }
            else -> {
                null
            }
        }

    }

}