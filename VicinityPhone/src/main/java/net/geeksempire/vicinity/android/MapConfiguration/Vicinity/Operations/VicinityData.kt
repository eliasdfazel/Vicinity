/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 9/5/20 8:30 AM
 * Last modified 9/5/20 8:28 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.MapConfiguration.Vicinity.Operations

data class VicinityData (var centerLatitude: String, var centerLongitude: String,
                         var countryName: String, val cityName: String,
                         var knownAddress: String, var approximateIpAddress: String)