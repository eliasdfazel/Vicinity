/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 9/8/20 6:29 AM
 * Last modified 9/8/20 5:49 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.CommunicationConfiguration.Public.UI

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import net.geeksempire.vicinity.android.databinding.PublicCommunityViewBinding

class PublicCommunity : AppCompatActivity() {

    object Configurations {
        const val PublicCommunityName: String = "PublicCommunityName"
    }

    lateinit var publicCommunityViewBinding: PublicCommunityViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        publicCommunityViewBinding = PublicCommunityViewBinding.inflate(layoutInflater)
        setContentView(publicCommunityViewBinding.root)

    }

}