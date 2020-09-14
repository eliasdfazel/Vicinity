/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 9/14/20 8:30 AM
 * Last modified 9/14/20 8:03 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.Preferences

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import net.geeksempire.vicinity.android.AccountManager.UI.AccountInformation
import net.geeksempire.vicinity.android.R
import net.geeksempire.vicinity.android.databinding.PreferencesControlViewBinding

class PreferencesControl : AppCompatActivity() {

    lateinit var preferencesControlViewBinding: PreferencesControlViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferencesControlViewBinding = PreferencesControlViewBinding.inflate(layoutInflater)
        setContentView(preferencesControlViewBinding.root)


        preferencesControlViewBinding.accountManagerView.setOnClickListener {

            startActivity(Intent(applicationContext, AccountInformation::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }, ActivityOptions.makeCustomAnimation(applicationContext, R.anim.fade_in, R.anim.fade_out).toBundle())

        }

    }

}