package org.linphone.compatibility

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.pm.ShortcutManager
import org.linphone.core.ChatRoom
import org.linphone.core.tools.Log
import org.linphone.utils.LinphoneUtils

@TargetApi(30)
class Api30Compatibility {
    companion object {
        fun hasReadPhoneNumbersPermission(context: Context): Boolean {
            val granted = Compatibility.hasPermission(context, Manifest.permission.READ_PHONE_NUMBERS)
            if (granted) {
                Log.d("[Permission Helper] Permission READ_PHONE_NUMBERS is granted")
            } else {
                Log.w("[Permission Helper] Permission READ_PHONE_NUMBERS is denied")
            }
            return granted
        }

        fun requestReadPhoneNumbersPermission(activity: Activity, code: Int) {
            activity.requestPermissions(arrayOf(Manifest.permission.READ_PHONE_NUMBERS), code)
        }

        fun removeChatRoomShortcut(context: Context, chatRoom: ChatRoom) {
            val shortcutManager = context.getSystemService(ShortcutManager::class.java)
            val id = LinphoneUtils.getChatRoomId(chatRoom.localAddress, chatRoom.peerAddress)
            val shortcutsToRemoveList = arrayListOf(id)
            shortcutManager.removeLongLivedShortcuts(shortcutsToRemoveList)
        }
    }
}
