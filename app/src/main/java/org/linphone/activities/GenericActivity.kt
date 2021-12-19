package org.linphone.activities

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Display
import android.view.Surface
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.navigation.ActivityNavigator
import androidx.window.layout.FoldingFeature
import androidx.window.layout.WindowInfoRepository.Companion.windowInfoRepository
import androidx.window.layout.WindowLayoutInfo
import java.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.linphone.IntelewebApplication.Companion.coreContext
import org.linphone.IntelewebApplication.Companion.corePreferences
import org.linphone.IntelewebApplication.Companion.ensureCoreExists
import org.linphone.R
import org.linphone.core.tools.Log

abstract class GenericActivity : AppCompatActivity() {
    private var timer: Timer? = null
    private var _isDestructionPending = false
    val isDestructionPending: Boolean
        get() = _isDestructionPending

    open fun onLayoutChanges(foldingFeature: FoldingFeature?) { }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ensureCoreExists(applicationContext)

        lifecycleScope.launch(Dispatchers.Main) {
            windowInfoRepository().windowLayoutInfo.collect { newLayoutInfo ->
                updateCurrentLayout(newLayoutInfo)
            }
        }

        requestedOrientation = if (corePreferences.forcePortrait) {
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } else {
            ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }

        _isDestructionPending = false
        val nightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val darkModeEnabled = corePreferences.darkMode
        when (nightMode) {
            Configuration.UI_MODE_NIGHT_NO, Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                if (darkModeEnabled == 1) {
                    // Force dark mode
                    Log.w("[Generic Activity] Forcing night mode")
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    _isDestructionPending = true
                }
            }
            Configuration.UI_MODE_NIGHT_YES -> {
                if (darkModeEnabled == 0) {
                    // Force light mode
                    Log.w("[Generic Activity] Forcing day mode")
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    _isDestructionPending = true
                }
            }
        }

        updateScreenSize()
    }

    override fun onResume() {
        super.onResume()

        var degrees = 270
        val orientation = windowManager.defaultDisplay.rotation
        when (orientation) {
            Surface.ROTATION_0 -> degrees = 0
            Surface.ROTATION_90 -> degrees = 270
            Surface.ROTATION_180 -> degrees = 180
            Surface.ROTATION_270 -> degrees = 90
        }
        Log.i("[Generic Activity] Device orientation is $degrees (raw value is $orientation)")
        val rotation = (360 - degrees) % 360
        coreContext.core.deviceRotation = rotation

        // Remove service notification if it has been started by device boot
        coreContext.notificationsManager.stopForegroundNotificationIfPossible()
    }

    override fun finish() {
        super.finish()
        ActivityNavigator.applyPopAnimationsToPendingTransition(this)
    }

    fun isTablet(): Boolean {
        return resources.getBoolean(R.bool.isTablet)
    }

    private fun updateScreenSize() {
        val metrics = DisplayMetrics()
        val display: Display = windowManager.defaultDisplay
        display.getRealMetrics(metrics)
        val screenWidth = metrics.widthPixels.toFloat()
        val screenHeight = metrics.heightPixels.toFloat()
        coreContext.screenWidth = screenWidth
        coreContext.screenHeight = screenHeight
    }

    private fun updateCurrentLayout(newLayoutInfo: WindowLayoutInfo) {
        if (newLayoutInfo.displayFeatures.isEmpty()) {
            onLayoutChanges(null)
        } else {
            for (feature in newLayoutInfo.displayFeatures) {
                val foldingFeature = feature as? FoldingFeature
                if (foldingFeature != null) {
                    onLayoutChanges(foldingFeature)
                }
            }
        }
    }
}
