/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 8/29/20 8:34 AM
 * Last modified 8/29/20 8:27 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.Utils.DependencyInjections.SubComponents

import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import dagger.BindsInstance
import dagger.Subcomponent
import net.geeksempire.vicinity.android.EntryConfiguration
import net.geeksempire.vicinity.android.Utils.DependencyInjections.Modules.Network.NetworkConnectionModule
import net.geeksempire.vicinity.android.Utils.DependencyInjections.Scopes.ActivityScope

@ActivityScope
@Subcomponent(modules = [NetworkConnectionModule::class])
interface NetworkSubDependencyGraph {

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance appCompatActivity: AppCompatActivity, @BindsInstance constraintLayout: ConstraintLayout): NetworkSubDependencyGraph
    }

    fun inject(entryConfiguration: EntryConfiguration)
}