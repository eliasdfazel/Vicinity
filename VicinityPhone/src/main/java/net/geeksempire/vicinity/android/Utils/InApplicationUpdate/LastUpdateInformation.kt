/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 10/12/20 11:17 AM
 * Last modified 10/12/20 11:16 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.Utils.InApplicationUpdate

import android.content.Context
import net.geeksempire.vicinity.android.BuildConfig
import net.geeksempire.vicinity.android.Utils.Data.FileIO

class LastUpdateInformation (private val context: Context){

    fun isApplicationUpdated() : Boolean{

        val fileIO: FileIO = FileIO(context)

        return (BuildConfig.VERSION_CODE > fileIO.readFile(".Updated")?.toInt()?:0)
    }
}