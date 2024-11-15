package com.mojise.library.chocolate.modal

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.fragment.app.FragmentActivity

enum class AxModalLoadingStyle {
    SequenceDots1, // Default
//    SequenceDots2,
//    SequenceDots3,
//    SequenceDots4,
//    TripleCircles,
//    Spinner,
//    IOS,
//    PaperPlane,
}

class AxModalLoadingDialog @JvmOverloads constructor(
    private val fragmentActivity: FragmentActivity,
    @ColorRes private val loadingColorResId: Int? = null,
    @StringRes private val loadingMessageResId: Int? = null,
) {
    val dialogFragment = AxModalLoadingDialogFragment.newInstance(
        loadingStyle = null,
        loadingColorResId = loadingColorResId,
        loadingMessageResId = loadingMessageResId,
    )

    val isVisible: Boolean
        get() = dialogFragment.isVisible

    fun show() {
        dialogFragment.show(fragmentActivity.supportFragmentManager)
    }

    fun hide() {
        dialogFragment.hide()
    }

    object GlobalOption {

        internal var loadingStyle: AxModalLoadingStyle? = null

        @ColorRes
        @JvmStatic
        var loadingColorResId: Int? = null

        @StringRes
        @JvmStatic
        var loadingMessageResId: Int? = null
    }
}