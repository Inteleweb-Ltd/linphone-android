package org.linphone.activities.main.chat.data

import org.linphone.IntelewebApplication.Companion.coreContext
import org.linphone.R
import org.linphone.core.ChatRoomSecurityLevel
import org.linphone.core.ParticipantDevice

class DevicesListChildData(private val device: ParticipantDevice) {
    val deviceName: String = device.name.orEmpty()

    val securityLevelIcon: Int by lazy {
        when (device.securityLevel) {
            ChatRoomSecurityLevel.Safe -> R.drawable.security_2_indicator
            ChatRoomSecurityLevel.Encrypted -> R.drawable.security_1_indicator
            else -> R.drawable.security_alert_indicator
        }
    }

    val securityContentDescription: Int by lazy {
        when (device.securityLevel) {
            ChatRoomSecurityLevel.Safe -> R.string.content_description_security_level_safe
            ChatRoomSecurityLevel.Encrypted -> R.string.content_description_security_level_encrypted
            else -> R.string.content_description_security_level_unsafe
        }
    }

    fun onClick() {
        coreContext.startCall(device.address, true)
    }
}
