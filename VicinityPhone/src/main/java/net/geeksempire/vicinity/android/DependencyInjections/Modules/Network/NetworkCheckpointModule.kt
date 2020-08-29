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

import dagger.Binds
import dagger.Module
import net.geeksempire.vicinity.android.Utils.Network.InterfaceNetworkCheckpoint
import net.geeksempire.vicinity.android.Utils.Network.NetworkCheckpoint

@Module
abstract class NetworkCheckpointModule {

    @Binds
    abstract fun provideNetworkCheckpoint(networkCheckpoint: NetworkCheckpoint/*This is Instance Of Return Type*/): InterfaceNetworkCheckpoint
}