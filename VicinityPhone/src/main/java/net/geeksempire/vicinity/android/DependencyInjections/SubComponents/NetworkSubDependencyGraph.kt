/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 8/29/20 9:22 AM
 * Last modified 8/29/20 8:58 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.DependencyInjections.SubComponents

import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import dagger.BindsInstance
import dagger.Subcomponent
import net.geeksempire.vicinity.android.DependencyInjections.Modules.Network.NetworkConnectionModule
import net.geeksempire.vicinity.android.DependencyInjections.Scopes.ActivityScope
import net.geeksempire.vicinity.android.EntryConfiguration

@ActivityScope
@Subcomponent(modules = [NetworkConnectionModule::class])
interface NetworkSubDependencyGraph {

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance appCompatActivity: AppCompatActivity, @BindsInstance constraintLayout: ConstraintLayout): NetworkSubDependencyGraph
    }

    fun inject(entryConfiguration: EntryConfiguration)

}