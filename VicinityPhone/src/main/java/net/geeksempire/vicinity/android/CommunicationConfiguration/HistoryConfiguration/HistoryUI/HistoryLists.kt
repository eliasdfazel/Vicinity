/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 10/17/20 6:37 AM
 * Last modified 10/17/20 6:37 AM
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
import net.geeksempire.vicinity.android.databinding.HistoryListsViewBinding
import net.geekstools.floatshort.PRO.Utils.UI.Gesture.GestureConstants
import net.geekstools.floatshort.PRO.Utils.UI.Gesture.GestureListenerConstants
import net.geekstools.floatshort.PRO.Utils.UI.Gesture.GestureListenerInterface

class HistoryLists : AppCompatActivity(), GestureListenerInterface {

    val historyLiveData: HistoryLiveData by lazy {
        ViewModelProvider(this@HistoryLists).get(HistoryLiveData::class.java)
    }

    lateinit var historyListsViewBinding: HistoryListsViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        historyListsViewBinding = HistoryListsViewBinding.inflate(layoutInflater)
        setContentView(historyListsViewBinding.root)

        historyLiveData.publicCommunicationHistory.observe(this@HistoryLists, Observer {



        })

        historyLiveData.privateCommunicationHistory.observe(this@HistoryLists, Observer {



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

}