package com.mojise.library.chocolate.modal

import android.util.Log
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.mojise.library.chocolate._internal.TAG

class AxModalLoadingDialog2(
    private val fragmentActivity: FragmentActivity,
    @ColorRes private val loadingColorResId: Int? = null,
    @StringRes private val loadingMessageResId: Int? = null,
) : DefaultLifecycleObserver {

    val isVisible: Boolean
        get() = dialogFragment.isVisible

    val dialogFragment = AxModalLoadingDialogFragment.newInstance(
        loadingStyle = null,
        loadingColorResId = loadingColorResId,
        loadingMessageResId = loadingMessageResId,
    )

    init {
        Log.e(TAG, "AxModalLoadingDialog2: fragmentActivity=$fragmentActivity")
        fragmentActivity.lifecycle.addObserver(this)
    }

    fun show() {
        dialogFragment.show(fragmentActivity.supportFragmentManager)
    }

    fun hide() {
        dialogFragment.hide()
    }

    override fun onCreate(owner: LifecycleOwner) {
        Log.w(TAG, "onCreate()")
    }

    override fun onDestroy(owner: LifecycleOwner) {
        Log.w(TAG, buildString {
            appendLine("onDestroy()")
            appendLine("fragmentManager=${fragmentActivity.supportFragmentManager.isDestroyed}")
            appendLine("fragmentManager=${fragmentActivity.supportFragmentManager.isStateSaved}")
        })

        //dialogFragment.hide()
    }
}