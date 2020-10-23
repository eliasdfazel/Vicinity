/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 10/23/20 6:03 AM
 * Last modified 10/23/20 5:48 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.MapConfiguration.Map.InformationWindow

import androidx.annotation.Keep
import com.google.firebase.firestore.DocumentSnapshot

@Keep
data class InformationWindowData (var userDocument: DocumentSnapshot)