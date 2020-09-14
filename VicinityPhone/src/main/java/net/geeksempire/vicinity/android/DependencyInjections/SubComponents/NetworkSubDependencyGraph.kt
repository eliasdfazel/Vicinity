/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 9/14/20 8:30 AM
 * Last modified 9/14/20 8:03 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.DependencyInjections.SubComponents

import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import dagger.BindsInstance
import dagger.Subcomponent
import net.geeksempire.vicinity.android.AccountManager.UI.AccountInformation
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

    fun inject(accountInformation: AccountInformation)
    fun inject(mapsOfSociety: MapsOfSociety)
    fun inject(mapsOfSociety: PublicCommunity)

}