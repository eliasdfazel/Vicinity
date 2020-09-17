/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 9/17/20 9:59 AM
 * Last modified 9/17/20 9:50 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.MapConfiguration.Vicinity.DataStructure

data class VicinityData (var centerLatitude: String, var centerLongitude: String,
                         var countryName: String, var cityName: String?,
                         var knownAddress: String?, var approximateIpAddress: String?)