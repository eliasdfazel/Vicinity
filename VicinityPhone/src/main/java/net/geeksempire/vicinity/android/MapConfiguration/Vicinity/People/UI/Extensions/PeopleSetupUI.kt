/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 12/8/20 11:28 AM
 * Last modified 12/8/20 11:23 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.MapConfiguration.Vicinity.People.UI.Extensions

import net.geeksempire.vicinity.android.MapConfiguration.Vicinity.People.UI.ListOfPeople
import net.geeksempire.vicinity.android.Utils.UI.Display.DpToInteger
import net.geeksempire.vicinity.android.Utils.UI.Display.navigationBarHeight
import net.geeksempire.vicinity.android.Utils.UI.Display.statusBarHeight

fun ListOfPeople.peopleSetupUserInterface() {

    peopleListViewBinding.root.setPadding(0,
        statusBarHeight(requireContext()) + DpToInteger(requireContext(), 61),
        0,
        navigationBarHeight(requireContext()) + DpToInteger(requireContext(), 61))


}