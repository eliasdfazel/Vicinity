/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 8/29/20 8:34 AM
 * Last modified 8/29/20 8:27 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.Utils.DependencyInjections

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import net.geeksempire.vicinity.android.EntryConfiguration
import net.geeksempire.vicinity.android.Utils.DependencyInjections.Modules.Network.NetworkCheckpointModule
import net.geeksempire.vicinity.android.Utils.DependencyInjections.Modules.SubDependencyGraphs
import net.geeksempire.vicinity.android.Utils.DependencyInjections.SubComponents.NetworkSubDependencyGraph
import javax.inject.Singleton

@Singleton
@Component (modules = [NetworkCheckpointModule::class, SubDependencyGraphs::class])
interface DependencyGraph {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): DependencyGraph
    }

    fun subDependencyGraph(): NetworkSubDependencyGraph.Factory

    fun inject(entryConfiguration: EntryConfiguration)
}
