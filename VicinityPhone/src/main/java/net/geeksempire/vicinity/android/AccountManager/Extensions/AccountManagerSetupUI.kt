/*
 * Copyright © 2020 By Geeks Empire.
 *
 * Created by Elias Fazel on 9/16/20 4:03 AM
 * Last modified 9/16/20 3:29 AM
 *
 * Licensed Under MIT License.
 * https://opensource.org/licenses/MIT
 */

package net.geeksempire.vicinity.android.AccountManager.Extensions

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import net.geeksempire.vicinity.android.AccountManager.DataStructure.UserInformationProfileData
import net.geeksempire.vicinity.android.AccountManager.UI.AccountInformation
import net.geeksempire.vicinity.android.AccountManager.Utils.UserInformation
import net.geeksempire.vicinity.android.R
import net.geeksempire.vicinity.android.Utils.UI.Colors.extractDominantColor
import net.geeksempire.vicinity.android.Utils.UI.Colors.extractVibrantColor
import net.geeksempire.vicinity.android.Utils.UI.Colors.isColorDark
import net.geeksempire.vicinity.android.Utils.UI.Display.statusBarHeight
import net.geeksempire.vicinity.android.Utils.UI.Theme.ThemeType

fun AccountInformation.accountManagerSetupUI() {

    when (overallTheme.checkThemeLightDark()) {
        ThemeType.ThemeLight -> {

            accountViewBinding.rootView.setBackgroundColor(getColor(R.color.white))

            accountViewBinding.welcomeTextView.setTextColor(getColor(R.color.dark))

        }
        ThemeType.ThemeDark -> {

            accountViewBinding.rootView.setBackgroundColor(getColor(R.color.black))

            accountViewBinding.welcomeTextView.setTextColor(getColor(R.color.light))

        }
    }

    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.navigationBarColor = Color.TRANSPARENT
    window.statusBarColor = Color.TRANSPARENT

    accountViewBinding.welcomeTextView.text = getString(R.string.welcomeText, if (firebaseAuth.currentUser != null) {
        firebaseAuth.currentUser?.displayName
    } else {
        ""
    })

    var dominantColor = getColor(R.color.yellow)
    var vibrantColor = getColor(R.color.default_color_light)

    window.setBackgroundDrawable(GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, arrayOf(vibrantColor, dominantColor).toIntArray()))

    val accountViewLayoutParameters: ConstraintLayout.LayoutParams = accountViewBinding.profileImageView.layoutParams as ConstraintLayout.LayoutParams
    accountViewLayoutParameters.setMargins(accountViewLayoutParameters.topMargin, accountViewLayoutParameters.topMargin + statusBarHeight(applicationContext), accountViewLayoutParameters.topMargin, accountViewLayoutParameters.topMargin)
    accountViewBinding.profileImageView.layoutParams = accountViewLayoutParameters

    firebaseAuth.currentUser?.let { firebaseUser ->

        Glide.with(this@accountManagerSetupUI)
            .asDrawable()
            .load(firebaseUser.photoUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .listener(object : RequestListener<Drawable> {

                override fun onLoadFailed(glideException: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {

                    return false
                }

                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {

                    runOnUiThread {

                        accountViewBinding.profileImageView.setImageDrawable(resource)

                        resource?.let {

                            dominantColor = extractDominantColor(applicationContext, it)
                            vibrantColor = extractVibrantColor(applicationContext, it)

                            window.setBackgroundDrawable(GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, arrayOf(vibrantColor, dominantColor).toIntArray()))


                            if (isColorDark(dominantColor) && isColorDark(vibrantColor)) {
                                Log.d(this@accountManagerSetupUI.javaClass.simpleName, "Dark Extracted Colors")

                                window.decorView.systemUiVisibility = 0

                                accountViewBinding.welcomeTextView.setTextColor(getColor(R.color.light))

                            } else {
                                Log.d(this@accountManagerSetupUI.javaClass.simpleName, "Light Extracted Colors")

                                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                                }

                                accountViewBinding.welcomeTextView.setTextColor(getColor(R.color.dark))

                            }

                        }

                    }

                    return false
                }

            })
            .submit()

        clickSetup()

    }

}

fun AccountInformation.clickSetup() {

    accountViewBinding.nextSubmitView.setOnClickListener {

        if (profileUpdate) {

            profileUpdate = false

            this@clickSetup.finish()

        } else {

            firebaseAuth.currentUser?.let { firebaseUser ->

                accountViewBinding.loadingView.visibility = View.VISIBLE
                accountViewBinding.loadingView.playAnimation()

                val userInformationProfileData: UserInformationProfileData = UserInformationProfileData(
                    userIdentification = firebaseUser.uid, userEmailAddress = firebaseUser.email.toString(), userDisplayName = firebaseUser.displayName.toString(), userProfileImage = firebaseUser.photoUrl.toString(),
                    userLatitude= userInformationIO.readUserLocation()?.latitude.toString(),  userLongitude = userInformationIO.readUserLocation()?.longitude.toString(),
                    instagramAccount = accountViewBinding.instagramAddressView.text.toString(),
                    twitterAccount = accountViewBinding.twitterAddressView.text.toString(),
                    phoneNumber = accountViewBinding.phoneNumberAddressView.text.toString(),
                )

                firestoreDatabase
                    .document(UserInformation.userProfileDatabasePath(firebaseUser.uid))
                    .set(userInformationProfileData)
                    .addOnSuccessListener {

                        accountViewBinding.loadingView.pauseAnimation()

                        accountViewBinding.loadingView.startAnimation(AnimationUtils.loadAnimation(applicationContext, R.anim.fade_out))
                        accountViewBinding.loadingView.visibility = View.INVISIBLE

                        Handler().postDelayed({

                            accountViewBinding.nextSubmitView.playAnimation()

                            profileUpdate = true

                        }, 731)

                    }

            }

        }

    }

}