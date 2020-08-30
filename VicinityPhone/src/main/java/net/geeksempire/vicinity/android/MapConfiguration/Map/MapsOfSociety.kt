/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 8/30/20 6:54 AM
 * Last modified 8/30/20 6:54 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.MapConfiguration.Map

import android.app.ActivityOptions
import android.app.PictureInPictureParams
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import net.geeksempire.vicinity.android.R
import net.geeksempire.vicinity.android.Utils.Networking.NetworkConnectionListener
import net.geeksempire.vicinity.android.Utils.Networking.NetworkConnectionListenerInterface
import net.geeksempire.vicinity.android.VicinityApplication
import net.geeksempire.vicinity.android.databinding.MapsViewBinding
import javax.inject.Inject

class MapsOfSociety : AppCompatActivity(), OnMapReadyCallback, NetworkConnectionListenerInterface,
    GoogleMap.OnMarkerClickListener,
    GoogleMap.OnMapLongClickListener, GoogleMap.OnMapClickListener,
    GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraIdleListener {

    lateinit var readyGoogleMap: GoogleMap

    private val mapView: SupportMapFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
    }

    var googleMapIsReady: Boolean = false

    @Inject lateinit var networkConnectionListener: NetworkConnectionListener

    lateinit var mapsViewBinding: MapsViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mapsViewBinding = MapsViewBinding.inflate(layoutInflater)
        setContentView(mapsViewBinding.root)

        (application as VicinityApplication)
            .dependencyGraph
            .subDependencyGraph()
            .create(this@MapsOfSociety, mapsViewBinding.rootView)
            .inject(this@MapsOfSociety)

        networkConnectionListener.networkConnectionListenerInterface = this@MapsOfSociety

    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()

        networkConnectionListener.unregisterDefaultNetworkCallback()

    }

    override fun onBackPressed() {

        startActivity(Intent(Intent.ACTION_MAIN).apply {
            this.addCategory(Intent.CATEGORY_HOME)
            this.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }, ActivityOptions.makeCustomAnimation(applicationContext, android.R.anim.fade_in, android.R.anim.fade_out).toBundle())

    }

    override fun enterPictureInPictureMode(pictureInPictureParams: PictureInPictureParams): Boolean {



        return super.enterPictureInPictureMode(pictureInPictureParams)
    }

    override fun networkAvailable() {

        /*
        *
        * Start Maps Operations
        *
        * */
        mapView.getMapAsync(this@MapsOfSociety)


    }

    override fun networkLost() {

    }

    override fun onMapReady(googleMap: GoogleMap) {
        readyGoogleMap = googleMap



        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        readyGoogleMap.addMarker(
            MarkerOptions()
                .position(sydney)
                .title("Marker in Sydney"))
        readyGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        TODO("Not yet implemented")
    }

    override fun onMapLongClick(latLng: LatLng?) {
        TODO("Not yet implemented")
    }

    override fun onMapClick(latLng: LatLng?) {
        TODO("Not yet implemented")
    }

    override fun onCameraMove() {
        TODO("Not yet implemented")
    }

    override fun onCameraIdle() {
        TODO("Not yet implemented")
    }

}