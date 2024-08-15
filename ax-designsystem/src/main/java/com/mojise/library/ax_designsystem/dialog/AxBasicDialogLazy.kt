package com.mojise.library.ax_designsystem.dialog

import android.app.Activity
import androidx.annotation.StringRes
import androidx.core.text.HtmlCompat

class AxBasicDialogLazy(
    private val dialogUiModel: AxBasicDialogFragment.UiModel,
) : Lazy<AxBasicDialogFragment> {

    private var cached: AxBasicDialogFragment? = null

    override val value: AxBasicDialogFragment
        get() = cached ?: AxBasicDialogFragment.newInstance(dialogUiModel)
            .also { cached = it }

    override fun isInitialized(): Boolean = (cached != null)
}

fun Activity.axBasicOneButtonDialog(
    title: String? = null,
    @StringRes content: Int,
    confirmButtonText: String? = null,
): Lazy<AxBasicDialogFragment> {
    val dialogUiModel = AxBasicDialogFragment.UiModel(
        dialogType = AxBasicDialogFragment.Type.OneButton,
        title = title ?: "알림",
        content = HtmlCompat.fromHtml(getString(content), HtmlCompat.FROM_HTML_MODE_LEGACY).toString(),
        positiveButtonText = confirmButtonText ?: "확인",
        negativeButtonText = "",
    )
    return AxBasicDialogLazy(dialogUiModel)
}

fun Activity.axBasicTwoButtonDialog(
    title: String? = null,
    @StringRes content: Int,
    negativeButtonText: String? = null,
    positiveButtonText: String? = null,
): Lazy<AxBasicDialogFragment> {
    val dialogUiModel = AxBasicDialogFragment.UiModel(
        dialogType = AxBasicDialogFragment.Type.TwoButton,
        title = title ?: "알림",
        content = HtmlCompat.fromHtml(getString(content), HtmlCompat.FROM_HTML_MODE_LEGACY).toString(),
        negativeButtonText = negativeButtonText ?: "취소",
        positiveButtonText = positiveButtonText ?: "확인",
    )
    return AxBasicDialogLazy(dialogUiModel)
}