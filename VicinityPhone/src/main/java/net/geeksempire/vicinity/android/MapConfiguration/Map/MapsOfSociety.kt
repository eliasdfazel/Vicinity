/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 9/1/20 5:11 AM
 * Last modified 9/1/20 5:08 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.MapConfiguration.Map

import android.app.ActivityOptions
import android.app.PictureInPictureParams
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.os.StrictMode
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import net.geeksempire.vicinity.android.MapConfiguration.Extensions.drawVicinity
import net.geeksempire.vicinity.android.MapConfiguration.Extensions.getLocationData
import net.geeksempire.vicinity.android.MapConfiguration.Extensions.setupGoogleMap
import net.geeksempire.vicinity.android.MapConfiguration.LocationDataHolder.MapsLiveData
import net.geeksempire.vicinity.android.MapConfiguration.Utils.MapsMarker
import net.geeksempire.vicinity.android.R
import net.geeksempire.vicinity.android.Utils.Location.LocationCheckpoint
import net.geeksempire.vicinity.android.Utils.Networking.NetworkCheckpoint
import net.geeksempire.vicinity.android.Utils.Networking.NetworkConnectionListener
import net.geeksempire.vicinity.android.Utils.Networking.NetworkConnectionListenerInterface
import net.geeksempire.vicinity.android.VicinityApplication
import net.geeksempire.vicinity.android.databinding.MapsViewBinding
import javax.inject.Inject

class MapsOfSociety : AppCompatActivity(), OnMapReadyCallback, NetworkConnectionListenerInterface,
    GoogleMap.OnMarkerClickListener,
    GoogleMap.OnMapLongClickListener, GoogleMap.OnMapClickListener,
    GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraIdleListener {

    companion object {
        const val GpsEnableRequestCode: Int = 111
    }

    val mapsLiveData: MapsLiveData by lazy {
        ViewModelProvider(this@MapsOfSociety).get(MapsLiveData::class.java)
    }

    val locationCheckpoint: LocationCheckpoint = LocationCheckpoint()

    val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(this@MapsOfSociety)
    }

    lateinit var readyGoogleMap: GoogleMap

    val mapView: SupportMapFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
    }

    val locationManager: LocationManager by lazy {
        applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    lateinit var userMapMarker: Marker

    val mapsMarker: MapsMarker by lazy {
        MapsMarker(this@MapsOfSociety, firebaseUser, readyGoogleMap, userMapMarker)
    }

    var userLatitudeLongitude: LatLng? = null

    var googleMapIsReady: Boolean = false

    val firebaseUser: FirebaseUser? = Firebase.auth.currentUser

    @Inject lateinit var networkCheckpoint: NetworkCheckpoint

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

        val builderStrictMode = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builderStrictMode.build())

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

        if (networkCheckpoint.networkConnection() && locationCheckpoint.gpsAvailable(applicationContext)) {


            getLocationData()

        } else {

            locationCheckpoint.turnOnGps(this@MapsOfSociety)

        }

    }

    override fun networkLost() {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            MapsOfSociety.GpsEnableRequestCode -> {

                if (networkCheckpoint.networkConnection() && locationCheckpoint.gpsAvailable(applicationContext)) {

                    getLocationData()

                } else {



                }

            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        readyGoogleMap = googleMap

        setupGoogleMap()

        userLatitudeLongitude?.let {

            readyGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(it, 15.0f))

            drawVicinity()

            googleMap.setOnCircleClickListener {



            }

            googleMap.setOnPolygonClickListener {



            }

        }

    }

    override fun onMarkerClick(marker: Marker?): Boolean {

        return true
    }

    override fun onMapLongClick(latLng: LatLng?) {

    }

    override fun onMapClick(latLng: LatLng?) {

    }

    override fun onCameraMove() {

    }

    override fun onCameraIdle() {

    }

}