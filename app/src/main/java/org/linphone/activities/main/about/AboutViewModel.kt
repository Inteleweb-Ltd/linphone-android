package org.linphone.activities.main.about

import androidx.lifecycle.ViewModel
import org.linphone.IntelewebApplication.Companion.coreContext

class AboutViewModel : ViewModel() {
    val appVersion: String = coreContext.appVersion

    val sdkVersion: String = coreContext.sdkVersion
}
