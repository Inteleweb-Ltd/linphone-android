package org.linphone.activities.main.chat.views

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import org.linphone.activities.main.chat.receivers.RichContentReceiver
import org.linphone.activities.main.viewmodels.SharedMainViewModel
import org.linphone.core.tools.Log
import org.linphone.utils.Event

/**
 * Allows for image input inside an EditText, usefull for keyboards with gif support for example.
 */
class RichEditText : AppCompatEditText {
    constructor(context: Context) : super(context) {
        initReceiveContentListener()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initReceiveContentListener()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initReceiveContentListener()
    }

    private fun initReceiveContentListener() {
        ViewCompat.setOnReceiveContentListener(
            this, RichContentReceiver.MIME_TYPES,
            RichContentReceiver { uri ->
                Log.i("[Rich Edit Text] Received URI: $uri")
                val activity = context as Activity
                val sharedViewModel = activity.run {
                    ViewModelProvider(activity as ViewModelStoreOwner)[SharedMainViewModel::class.java]
                }
                sharedViewModel.richContentUri.value = Event(uri)
            }
        )
    }
}
