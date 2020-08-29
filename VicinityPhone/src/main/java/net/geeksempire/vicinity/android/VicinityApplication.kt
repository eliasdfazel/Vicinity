/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 8/29/20 8:34 AM
 * Last modified 8/29/20 8:32 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android

import android.app.Application
import com.google.firebase.analytics.FirebaseAnalytics
import net.geeksempire.vicinity.android.Utils.DependencyInjections.DaggerDependencyGraph
import net.geeksempire.vicinity.android.Utils.DependencyInjections.DependencyGraph

class VicinityApplication : Application() {

    val dependencyGraph: DependencyGraph by lazy {
        DaggerDependencyGraph.factory().create(applicationContext)
    }

    override fun onCreate() {
        super.onCreate()

        val firebaseAnalytics = FirebaseAnalytics.getInstance(applicationContext)

        firebaseAnalytics.setAnalyticsCollectionEnabled(!BuildConfig.DEBUG)

    }

}