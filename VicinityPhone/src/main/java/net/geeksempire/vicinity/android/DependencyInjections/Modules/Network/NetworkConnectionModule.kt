/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 8/29/20 8:40 AM
 * Last modified 8/29/20 8:39 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.DependencyInjections.Modules.Network

import android.net.ConnectivityManager
import dagger.Binds
import dagger.Module
import net.geeksempire.vicinity.android.Utils.Network.NetworkConnectionListener

@Module
abstract class NetworkConnectionModule {

    @Binds
    abstract fun provideNetworkConnectionListener(networkConnectionListener: NetworkConnectionListener/*This is Instance Of Return Type*/): ConnectivityManager.NetworkCallback
}