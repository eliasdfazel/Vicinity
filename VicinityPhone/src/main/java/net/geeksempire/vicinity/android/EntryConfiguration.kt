/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 8/29/20 7:54 AM
 * Last modified 8/29/20 7:54 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import net.geeksempire.vicinity.android.databinding.EntryConfigurationViewBinding

class EntryConfiguration : AppCompatActivity() {

    lateinit var entryConfigurationViewBinding: EntryConfigurationViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        entryConfigurationViewBinding = EntryConfigurationViewBinding.inflate(layoutInflater)
        setContentView(entryConfigurationViewBinding.root)
    }
}