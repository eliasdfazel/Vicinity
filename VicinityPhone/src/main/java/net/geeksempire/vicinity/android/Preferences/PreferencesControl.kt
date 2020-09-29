/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 9/29/20 12:59 PM
 * Last modified 9/29/20 12:59 PM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.Preferences

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import net.geeksempire.vicinity.android.AccountManager.UI.AccountInformation
import net.geeksempire.vicinity.android.Preferences.DataHolder.PreferencesLiveData
import net.geeksempire.vicinity.android.Preferences.Extensions.preferencesControlSetupUI
import net.geeksempire.vicinity.android.Preferences.Extensions.toggleLightDark
import net.geeksempire.vicinity.android.R
import net.geeksempire.vicinity.android.Utils.UI.Theme.OverallTheme
import net.geeksempire.vicinity.android.Utils.UI.Theme.ThemeType
import net.geeksempire.vicinity.android.databinding.PreferencesControlViewBinding

class PreferencesControl : AppCompatActivity() {

    val overallTheme: OverallTheme by lazy {
        OverallTheme(applicationContext)
    }

    val firebaseUser: FirebaseUser = Firebase.auth.currentUser!!

    val preferencesLiveData: PreferencesLiveData by lazy {
        ViewModelProvider(this@PreferencesControl).get(PreferencesLiveData::class.java)
    }

    lateinit var preferencesControlViewBinding: PreferencesControlViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferencesControlViewBinding = PreferencesControlViewBinding.inflate(layoutInflater)
        setContentView(preferencesControlViewBinding.root)

        preferencesControlSetupUI()

        preferencesLiveData.toggleTheme.observe(this@PreferencesControl, Observer {

            var delayTheme: Long = 3333

            when(overallTheme.checkThemeLightDark()) {
                ThemeType.ThemeLight -> {
                    delayTheme = 3000
                }
                ThemeType.ThemeDark -> {
                    delayTheme = 1133
                }
            }

            if (it) {

                Handler(Looper.getMainLooper()).postDelayed({

                    toggleLightDark()

                }, delayTheme)

            } else {

                toggleLightDark()

            }

        })

        preferencesControlViewBinding.userDisplayName.text = firebaseUser.displayName

        Glide.with(this@PreferencesControl)
            .asDrawable()
            .load(firebaseUser.photoUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(preferencesControlViewBinding.userProfileImage)

        preferencesControlViewBinding.accountManagerView.setOnClickListener {

            startActivity(Intent(applicationContext, AccountInformation::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }, ActivityOptions.makeCustomAnimation(applicationContext, R.anim.fade_in, R.anim.fade_out).toBundle())

        }

    }

    override fun onBackPressed() {
        super.onBackPressed()

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)

    }

}