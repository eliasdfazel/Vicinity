/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 9/9/20 8:55 AM
 * Last modified 9/9/20 8:53 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.DependencyInjections.SubComponents

import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import dagger.BindsInstance
import dagger.Subcomponent
import net.geeksempire.vicinity.android.AccountManager.AccountSignIn
import net.geeksempire.vicinity.android.CommunicationConfiguration.Public.PublicCommunityUI.PublicCommunity
import net.geeksempire.vicinity.android.DependencyInjections.Modules.Network.NetworkCheckpointModule
import net.geeksempire.vicinity.android.DependencyInjections.Modules.Network.NetworkConnectionModule
import net.geeksempire.vicinity.android.DependencyInjections.Scopes.ActivityScope
import net.geeksempire.vicinity.android.MapConfiguration.Map.MapsOfSociety

@ActivityScope
@Subcomponent(modules = [NetworkConnectionModule::class, NetworkCheckpointModule::class])
interface NetworkSubDependencyGraph {

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance appCompatActivity: AppCompatActivity, @BindsInstance constraintLayout: ConstraintLayout): NetworkSubDependencyGraph
    }

    fun inject(accountSignIn: AccountSignIn)
    fun inject(mapsOfSociety: MapsOfSociety)
    fun inject(mapsOfSociety: PublicCommunity)

}