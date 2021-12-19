package org.linphone.activities.launcher

import android.content.Intent
import android.os.Bundle
import org.linphone.IntelewebApplication.Companion.coreContext
import org.linphone.IntelewebApplication.Companion.corePreferences
import org.linphone.R
import org.linphone.activities.GenericActivity
import org.linphone.activities.main.MainActivity
import org.linphone.core.tools.Log

class LauncherActivity : GenericActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.launcher_activity)
    }

    override fun onStart() {
        super.onStart()
        coreContext.handler.postDelayed({ onReady() }, 500)
    }

    private fun onReady() {
        Log.i("[Launcher] Core is ready")

        if (corePreferences.preventInterfaceFromShowingUp) {
            Log.w("[Context] We were asked to not show the user interface")
            finish()
            return
        }

        val intent = Intent()
        intent.setClass(this, MainActivity::class.java)

        // Propagate current intent action, type and data
        if (getIntent() != null) {
            val extras = getIntent().extras
            if (extras != null) intent.putExtras(extras)
        }
        intent.action = getIntent().action
        intent.type = getIntent().type
        intent.data = getIntent().data

        startActivity(intent)
        if (corePreferences.enableAnimations) {
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }
}
