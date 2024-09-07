package com.mojise.library.ax_designsystem.dialog

import androidx.annotation.StringRes
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment

fun Fragment.axBasicOneButtonDialog(
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

fun Fragment.axBasicTwoButtonDialog(
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