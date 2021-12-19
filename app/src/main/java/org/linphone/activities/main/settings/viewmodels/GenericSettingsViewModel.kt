package org.linphone.activities.main.settings.viewmodels

import androidx.lifecycle.ViewModel
import org.linphone.IntelewebApplication.Companion.coreContext
import org.linphone.IntelewebApplication.Companion.corePreferences

abstract class GenericSettingsViewModel : ViewModel() {
    protected val prefs = corePreferences
    protected val core = coreContext.core
}
