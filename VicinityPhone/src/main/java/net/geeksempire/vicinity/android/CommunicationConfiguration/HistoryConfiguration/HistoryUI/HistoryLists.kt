/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 10/18/20 8:25 AM
 * Last modified 10/18/20 8:25 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.CommunicationConfiguration.HistoryConfiguration.HistoryUI

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import net.geeksempire.vicinity.android.CommunicationConfiguration.HistoryConfiguration.DataStructure.HistoryLiveData
import net.geeksempire.vicinity.android.CommunicationConfiguration.HistoryConfiguration.Extensions.historyListsSetupUI
import net.geeksempire.vicinity.android.CommunicationConfiguration.HistoryConfiguration.Extensions.privateHistoryButtonBackgroundChange
import net.geeksempire.vicinity.android.CommunicationConfiguration.HistoryConfiguration.Extensions.publicHistoryButtonBackgroundChange
import net.geeksempire.vicinity.android.CommunicationConfiguration.HistoryConfiguration.HistoryUI.Adapter.HistoryListsAdapter
import net.geeksempire.vicinity.android.CommunicationConfiguration.HistoryConfiguration.HistoryUI.Adapter.HistoryType
import net.geeksempire.vicinity.android.CommunicationConfiguration.HistoryConfiguration.Utils.defaultHistoryView
import net.geeksempire.vicinity.android.CommunicationConfiguration.HistoryConfiguration.Utils.saveDefaultHistoryView
import net.geeksempire.vicinity.android.Utils.Networking.NetworkCheckpoint
import net.geeksempire.vicinity.android.Utils.Networking.NetworkConnectionListener
import net.geeksempire.vicinity.android.Utils.Networking.NetworkConnectionListenerInterface
import net.geeksempire.vicinity.android.Utils.UI.Theme.OverallTheme
import net.geeksempire.vicinity.android.VicinityApplication
import net.geeksempire.vicinity.android.databinding.HistoryListsViewBinding
import net.geekstools.floatshort.PRO.Utils.UI.Gesture.GestureConstants
import net.geekstools.floatshort.PRO.Utils.UI.Gesture.GestureListenerConstants
import net.geekstools.floatshort.PRO.Utils.UI.Gesture.GestureListenerInterface
import net.geekstools.floatshort.PRO.Utils.UI.Gesture.SwipeGestureListener
import javax.inject.Inject

class HistoryLists : AppCompatActivity(), GestureListenerInterface, NetworkConnectionListenerInterface {

    val overallTheme: OverallTheme by lazy {
        OverallTheme(applicationContext)
    }

    private val historyLiveData: HistoryLiveData by lazy {
        ViewModelProvider(this@HistoryLists).get(HistoryLiveData::class.java)
    }

    private val historyListsAdapter: HistoryListsAdapter by lazy {
        HistoryListsAdapter(this@HistoryLists)
    }

    private val swipeGestureListener: SwipeGestureListener by lazy {
        SwipeGestureListener(applicationContext, this@HistoryLists)
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

        historyListsSetupUI()

        historyListsViewBinding.historyRecyclerView.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)

        historyListsViewBinding.historyRecyclerView.adapter = historyListsAdapter

        historyLiveData.publicCommunicationHistory.observe(this@HistoryLists, Observer {

            historyListsAdapter.historyType = HistoryType.PUBLIC

            if (!it.isNullOrEmpty()) {

                historyListsAdapter.publicMessengerData.clear()
                historyListsAdapter.publicMessengerData.addAll(it)

                historyListsAdapter.notifyDataSetChanged()

                historyListsViewBinding.loadingView.visibility = View.INVISIBLE

                publicHistoryButtonBackgroundChange(applicationContext, historyListsViewBinding.publicHistory, historyListsViewBinding.privateHistory)

            } else {



            }

        })

        historyLiveData.privateCommunicationHistory.observe(this@HistoryLists, Observer {

            historyListsAdapter.historyType = HistoryType.PRIVATE

            if (!it.isNullOrEmpty()) {

                historyListsAdapter.privateMessengerData.clear()
                historyListsAdapter.privateMessengerData.addAll(it)

                historyListsAdapter.notifyDataSetChanged()

                historyListsViewBinding.loadingView.visibility = View.INVISIBLE

                privateHistoryButtonBackgroundChange(applicationContext, historyListsViewBinding.publicHistory, historyListsViewBinding.privateHistory)


            } else {



            }

        })

        when (defaultHistoryView(applicationContext)) {
            HistoryType.PUBLIC -> {

                historyListsViewBinding.loadingView.visibility = View.VISIBLE

                historyLiveData.publicHistoryNetworkOperation()

            }
            HistoryType.PRIVATE -> {

                historyListsViewBinding.loadingView.visibility = View.VISIBLE

                historyLiveData.privateHistoryNetworkOperation()

            }
        }

        historyListsViewBinding.publicHistory.setOnClickListener {

            historyListsViewBinding.loadingView.visibility = View.VISIBLE

            historyLiveData.publicHistoryNetworkOperation()

        }

        historyListsViewBinding.privateHistory.setOnClickListener {

            historyListsViewBinding.loadingView.visibility = View.VISIBLE

            historyLiveData.privateHistoryNetworkOperation()

        }

    }

    override fun onPause() {
        super.onPause()

        saveDefaultHistoryView(applicationContext, historyListsAdapter.historyType)

    }

    override fun onBackPressed() {

        this@HistoryLists.finish()

    }

    override fun dispatchTouchEvent(motionEvent: MotionEvent?): Boolean {
        motionEvent?.let {
            swipeGestureListener.onTouchEvent(it)
        }

        return super.dispatchTouchEvent(motionEvent)
    }

    override fun onSwipeGesture(gestureConstants: GestureConstants, downMotionEvent: MotionEvent, moveMotionEvent: MotionEvent, initVelocityX: Float, initVelocityY: Float) {
        super.onSwipeGesture(gestureConstants, downMotionEvent, moveMotionEvent, initVelocityX, initVelocityY)

        when (gestureConstants) {
            is GestureConstants.SwipeHorizontal -> {
                when (gestureConstants.horizontalDirection) {
                    GestureListenerConstants.SWIPE_RIGHT -> {

                        historyListsViewBinding.loadingView.visibility = View.VISIBLE

                        historyLiveData.publicHistoryNetworkOperation()

                    }
                    GestureListenerConstants.SWIPE_LEFT -> {

                        historyListsViewBinding.loadingView.visibility = View.VISIBLE

                        historyLiveData.privateHistoryNetworkOperation()

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