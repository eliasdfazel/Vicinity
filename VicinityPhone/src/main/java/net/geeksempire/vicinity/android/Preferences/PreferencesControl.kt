/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 9/13/20 8:01 AM
 * Last modified 9/13/20 8:00 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.Preferences

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import net.geeksempire.vicinity.android.databinding.PreferencesControlViewBinding

class PreferencesControl : AppCompatActivity() {

    lateinit var preferencesControlViewBinding: PreferencesControlViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferencesControlViewBinding = PreferencesControlViewBinding.inflate(layoutInflater)
        setContentView(preferencesControlViewBinding.root)

    }

}