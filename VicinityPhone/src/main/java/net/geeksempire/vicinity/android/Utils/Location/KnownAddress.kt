/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 9/17/20 9:31 AM
 * Last modified 9/17/20 9:16 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.Utils.Location

import android.content.Intent
import android.location.Address
import android.location.Location
import android.os.Bundle
import android.os.ResultReceiver
import androidx.core.app.JobIntentService
import com.google.android.gms.maps.model.LatLng

class KnownAddress : JobIntentService() {

    object Constants {
        const val SUCCESS_RESULT = 0
        const val FAILURE_RESULT = 1

        private const val PACKAGE_NAME = "net.geeksempire.vicinity.android"

        const val RESULT_RECEIVER = "$PACKAGE_NAME.RECEIVER"
        const val RESULT_DATA_KEY = "$PACKAGE_NAME.RESULT_DATA_KEY"
        const val LOCATION_DATA_EXTRA = "$PACKAGE_NAME.LOCATION_DATA_EXTRA"
    }

    private var resultReceiver: ResultReceiver? = null

    override fun onHandleWork(intent: Intent) {

        intent ?: return

        val locationCheckpoint = LocationCheckpoint()

        when (intent.action) {
            "LocationData" -> {
                resultReceiver = intent.getParcelableExtra<ResultReceiver>(Constants.RESULT_RECEIVER)

                val locationInfo = intent.getParcelableExtra<Location>(Constants.LOCATION_DATA_EXTRA)

                var geoAddresses: ArrayList<Address>? = null

                locationInfo?.let {

                    geoAddresses = locationCheckpoint.loadLocationInfo(applicationContext, LatLng(locationInfo.latitude, locationInfo.longitude))

                }

                if (geoAddresses != null) {

                    if (geoAddresses!!.isEmpty()) {
                        deliverResultToReceiver(Constants.FAILURE_RESULT, null)
                    } else {
                        deliverResultToReceiver(Constants.SUCCESS_RESULT, geoAddresses!![0].getAddressLine(0))
                    }

                } else {
                    deliverResultToReceiver(Constants.FAILURE_RESULT, null)
                }
            }
            "LatLongData" -> {
                resultReceiver = intent.getParcelableExtra<ResultReceiver>(Constants.RESULT_RECEIVER)

                val locationInfo = intent.getParcelableExtra<LatLng>(Constants.LOCATION_DATA_EXTRA)

                var geoAddresses: ArrayList<Address>? = null

                locationInfo?.let {

                    geoAddresses = locationCheckpoint.loadLocationInfo(applicationContext, LatLng(locationInfo.latitude, locationInfo.longitude))

                }

                if (geoAddresses != null) {
                    if (geoAddresses!!.isEmpty()) {
                        deliverResultToReceiver(Constants.FAILURE_RESULT, null)
                    } else {
                        deliverResultToReceiver(Constants.SUCCESS_RESULT, geoAddresses!![0].getAddressLine(0))
                    }
                } else {
                    deliverResultToReceiver(Constants.FAILURE_RESULT, null)
                }
            }
            else -> {
                deliverResultToReceiver(Constants.FAILURE_RESULT, null)
            }
        }

    }

    private fun deliverResultToReceiver(resultCode: Int, hintMessage: String?) {
        val bundle = Bundle().apply {
            putString(Constants.RESULT_DATA_KEY, hintMessage)
        }
        resultReceiver?.send(resultCode, bundle)
    }

}
