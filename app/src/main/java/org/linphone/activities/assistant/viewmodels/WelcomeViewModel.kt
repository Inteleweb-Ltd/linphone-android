package org.linphone.activities.assistant.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.linphone.IntelewebApplication.Companion.corePreferences

class WelcomeViewModel : ViewModel() {
    val showCreateAccount: Boolean = corePreferences.showCreateAccount
    val showLinphoneLogin: Boolean = corePreferences.showLinphoneLogin
    val showGenericLogin: Boolean = corePreferences.showGenericLogin
    val showRemoteProvisioning: Boolean = corePreferences.showRemoteProvisioning

    val termsAndPrivacyAccepted = MutableLiveData<Boolean>()

    init {
        termsAndPrivacyAccepted.value = corePreferences.readAndAgreeTermsAndPrivacy
    }
}
