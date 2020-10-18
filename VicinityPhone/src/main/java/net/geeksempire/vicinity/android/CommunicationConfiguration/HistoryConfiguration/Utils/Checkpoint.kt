/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 10/18/20 5:18 AM
 * Last modified 10/18/20 5:16 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.CommunicationConfiguration.HistoryConfiguration.Utils

import android.content.Context
import net.geeksempire.vicinity.android.CommunicationConfiguration.HistoryConfiguration.HistoryUI.Adapter.HistoryType
import net.geeksempire.vicinity.android.Utils.Preferences.ReadPreferences
import net.geeksempire.vicinity.android.Utils.Preferences.SavePreferences

fun defaultHistoryView(context: Context) : String {

    val readPreferences = ReadPreferences(context)

    return readPreferences.readPreference("History", "DefaultHistoryView", HistoryType.PUBLIC)!!
}

fun saveDefaultHistoryView(context: Context, historyType: String) {

    val savePreferences = SavePreferences(context)

    savePreferences.savePreference("History", "DefaultHistoryView", historyType)

}