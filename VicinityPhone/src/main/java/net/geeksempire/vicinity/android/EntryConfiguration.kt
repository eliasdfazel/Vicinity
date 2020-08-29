/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 8/29/20 8:34 AM
 * Last modified 8/29/20 8:34 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import net.geeksempire.vicinity.android.Utils.Network.NetworkConnectionListener
import net.geeksempire.vicinity.android.databinding.EntryConfigurationViewBinding
import javax.inject.Inject

class EntryConfiguration : AppCompatActivity() {

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

    }
}