package com.mojise.library.chocolate.dialog

import android.app.Activity
import androidx.annotation.StringRes
import androidx.core.text.HtmlCompat

fun Activity.axBasicOneButtonDialog(
    title: String? = null,
    @StringRes content: Int,
    confirmButtonText: String? = null,
): Lazy<com.mojise.library.chocolate.dialog.AxBasicDialogFragment> {
    val dialogUiModel = com.mojise.library.chocolate.dialog.AxBasicDialogFragment.UiModel(
        dialogType = com.mojise.library.chocolate.dialog.AxBasicDialogFragment.Type.OneButton,
        title = title ?: "알림",
        content = HtmlCompat.fromHtml(getString(content), HtmlCompat.FROM_HTML_MODE_LEGACY)
            .toString(),
        positiveButtonText = confirmButtonText ?: "확인",
        negativeButtonText = "",
    )
    return com.mojise.library.chocolate.dialog.AxBasicDialogLazy(dialogUiModel)
}

fun Activity.axBasicTwoButtonDialog(
    title: String? = null,
    @StringRes content: Int,
    negativeButtonText: String? = null,
    positiveButtonText: String? = null,
): Lazy<com.mojise.library.chocolate.dialog.AxBasicDialogFragment> {
    val dialogUiModel = com.mojise.library.chocolate.dialog.AxBasicDialogFragment.UiModel(
        dialogType = com.mojise.library.chocolate.dialog.AxBasicDialogFragment.Type.TwoButton,
        title = title ?: "알림",
        content = HtmlCompat.fromHtml(getString(content), HtmlCompat.FROM_HTML_MODE_LEGACY)
            .toString(),
        negativeButtonText = negativeButtonText ?: "취소",
        positiveButtonText = positiveButtonText ?: "확인",
    )
    return com.mojise.library.chocolate.dialog.AxBasicDialogLazy(dialogUiModel)
}