/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 10/17/20 6:45 AM
 * Last modified 10/17/20 6:43 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.CommunicationConfiguration.HistoryConfiguration.HistoryUI

import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import net.geeksempire.vicinity.android.CommunicationConfiguration.HistoryConfiguration.DataStructure.HistoryLiveData
import net.geeksempire.vicinity.android.Utils.Networking.NetworkCheckpoint
import net.geeksempire.vicinity.android.Utils.Networking.NetworkConnectionListener
import net.geeksempire.vicinity.android.Utils.Networking.NetworkConnectionListenerInterface
import net.geeksempire.vicinity.android.Utils.UI.Theme.OverallTheme
import net.geeksempire.vicinity.android.VicinityApplication
import net.geeksempire.vicinity.android.databinding.HistoryListsViewBinding
import net.geekstools.floatshort.PRO.Utils.UI.Gesture.GestureConstants
import net.geekstools.floatshort.PRO.Utils.UI.Gesture.GestureListenerConstants
import net.geekstools.floatshort.PRO.Utils.UI.Gesture.GestureListenerInterface
import javax.inject.Inject

class HistoryLists : AppCompatActivity(), GestureListenerInterface, NetworkConnectionListenerInterface {

    val overallTheme: OverallTheme by lazy {
        OverallTheme(applicationContext)
    }

    val historyLiveData: HistoryLiveData by lazy {
        ViewModelProvider(this@HistoryLists).get(HistoryLiveData::class.java)
    }

    @Inject lateinit var networkCheckpoint: NetworkCheckpoint

    @Inject lateinit var networkConnectionListener: NetworkConnectionListener

    lateinit var historyListsViewBinding: HistoryListsViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        historyListsViewBinding = HistoryListsViewBinding.inflate(layoutInflater)
        setContentView(historyListsViewBinding.root)

        (application as VicinityApplication)
            .dependencyGraph
            .subDependencyGraph()
            .create(this@HistoryLists, historyListsViewBinding.rootView)
            .inject(this@HistoryLists)

        networkConnectionListener.networkConnectionListenerInterface = this@HistoryLists

        historyLiveData.publicCommunicationHistory.observe(this@HistoryLists, Observer {

            if (!it.isNullOrEmpty()) {



            } else {



            }

        })

        historyLiveData.privateCommunicationHistory.observe(this@HistoryLists, Observer {

            if (!it.isNullOrEmpty()) {



            } else {



            }

        })

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onSwipeGesture(gestureConstants: GestureConstants, downMotionEvent: MotionEvent, moveMotionEvent: MotionEvent, initVelocityX: Float, initVelocityY: Float) {
        super.onSwipeGesture(gestureConstants, downMotionEvent, moveMotionEvent, initVelocityX, initVelocityY)

        when (gestureConstants) {
            is GestureConstants.SwipeHorizontal -> {
                when (gestureConstants.horizontalDirection) {
                    GestureListenerConstants.SWIPE_RIGHT -> {



                    }
                    GestureListenerConstants.SWIPE_LEFT -> {



                    }
                }
            }
        }

    }

    override fun networkAvailable() {



    }

    override fun networkLost() {



    }

}