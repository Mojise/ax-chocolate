package com.mojise.library.ax_designsystem.dialog

import androidx.fragment.app.Fragment

fun Fragment.axBasicOneButtonDialog(
    title: String? = null,
    content: String,
    confirmButtonText: String? = null,
): Lazy<AxBasicDialogFragment> {
    val dialogUiModel = AxBasicDialogFragment.UiModel(
        dialogType = AxBasicDialogFragment.Type.OneButton,
        title = title ?: "알림",
        content = content,
        positiveButtonText = confirmButtonText ?: "확인",
        negativeButtonText = "",
    )
    return AxBasicDialogLazy(dialogUiModel)
}

fun Fragment.axBasicTwoButtonDialog(
    title: String? = null,
    content: String,
    confirmButtonText: String? = null,
): Lazy<AxBasicDialogFragment> {
    val dialogUiModel = AxBasicDialogFragment.UiModel(
        dialogType = AxBasicDialogFragment.Type.OneButton,
        title = title ?: "알림",
        content = content,
        positiveButtonText = confirmButtonText ?: "확인",
        negativeButtonText = "",
    )
    return AxBasicDialogLazy(dialogUiModel)
}