/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 8/29/20 9:22 AM
 * Last modified 8/29/20 9:18 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import net.geeksempire.vicinity.android.Utils.Networking.NetworkConnectionListener
import net.geeksempire.vicinity.android.Utils.Networking.NetworkConnectionListenerInterface
import net.geeksempire.vicinity.android.databinding.EntryConfigurationViewBinding
import javax.inject.Inject

class EntryConfiguration : AppCompatActivity(), NetworkConnectionListenerInterface {

    @Inject
    lateinit var networkConnectionListener: NetworkConnectionListener

    private lateinit var entryConfigurationViewBinding: EntryConfigurationViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        entryConfigurationViewBinding = EntryConfigurationViewBinding.inflate(layoutInflater)
        setContentView(entryConfigurationViewBinding.root)

        (application as VicinityApplication)
            .dependencyGraph
            .subDependencyGraph()
            .create(this@EntryConfiguration, entryConfigurationViewBinding.rootView)
            .inject(this@EntryConfiguration)

        networkConnectionListener.networkConnectionListenerInterface = this@EntryConfiguration

    }

    override fun onDestroy() {
        super.onDestroy()

        networkConnectionListener.unregisterDefaultNetworkCallback()

    }

    override fun networkAvailable() {

    }

    override fun networkLost() {

    }
}