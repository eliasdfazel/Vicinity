/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 8/30/20 5:14 AM
 * Last modified 8/30/20 4:25 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.DependencyInjections

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import net.geeksempire.vicinity.android.DependencyInjections.Modules.Network.NetworkCheckpointModule
import net.geeksempire.vicinity.android.DependencyInjections.Modules.SubDependencyGraphs
import net.geeksempire.vicinity.android.DependencyInjections.SubComponents.NetworkSubDependencyGraph
import net.geeksempire.vicinity.android.EntryConfiguration
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
