/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 8/30/20 5:14 AM
 * Last modified 8/30/20 5:11 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import net.geeksempire.vicinity.android.Utils.Networking.NetworkCheckpoint
import net.geeksempire.vicinity.android.Utils.UI.NotifyUser.SnackbarActionHandlerInterface
import net.geeksempire.vicinity.android.Utils.UI.NotifyUser.SnackbarBuilder
import net.geeksempire.vicinity.android.databinding.EntryConfigurationViewBinding
import javax.inject.Inject

class EntryConfiguration : AppCompatActivity() {

    @Inject
    lateinit var networkCheckpoint: NetworkCheckpoint

    private lateinit var entryConfigurationViewBinding: EntryConfigurationViewBinding

    companion object {
        const val PermissionRequestCode: Int = 123
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        entryConfigurationViewBinding = EntryConfigurationViewBinding.inflate(layoutInflater)
        setContentView(entryConfigurationViewBinding.root)

        (application as VicinityApplication)
            .dependencyGraph
            .inject(this@EntryConfiguration)

        runtimePermission()

    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissionsList: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissionsList, grantResults)

        when (requestCode) {
            EntryConfiguration.PermissionRequestCode -> {

                if (checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.ACCESS_WIFI_STATE) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.VIBRATE) == PackageManager.PERMISSION_GRANTED) {

                    //

                } else {

                    SnackbarBuilder(applicationContext).show (
                        rootView = entryConfigurationViewBinding.rootView,
                        messageText= getString(R.string.permissionMessage),
                        messageDuration = Snackbar.LENGTH_INDEFINITE,
                        actionButtonText = R.string.grantPermission,
                        snackbarActionHandlerInterface = object : SnackbarActionHandlerInterface {

                            override fun onActionButtonClicked(snackbar: Snackbar) {
                                super.onActionButtonClicked(snackbar)

                                runtimePermission()

                            }

                        }
                    )

                }

            }
        }

    }

    private fun runtimePermission() {

        val permissionsList = arrayListOf(
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.VIBRATE
        )

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            permissionsList.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }

        requestPermissions(
            permissionsList.toTypedArray(),
            EntryConfiguration.PermissionRequestCode
        )

    }

}