/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 8/29/20 8:34 AM
 * Last modified 8/29/20 8:34 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.Utils.DependencyInjections.Modules

import dagger.Module
import net.geeksempire.vicinity.android.Utils.DependencyInjections.SubComponents.NetworkSubDependencyGraph

@Module(subcomponents = [NetworkSubDependencyGraph::class])
class SubDependencyGraphs