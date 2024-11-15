package com.mojise.library.chocolate.dialog

import androidx.fragment.app.Fragment

fun Fragment.axBasicOneButtonDialog(
    title: String? = null,
    content: String,
    confirmButtonText: String? = null,
): Lazy<com.mojise.library.chocolate.dialog.AxBasicDialogFragment> {
    val dialogUiModel = com.mojise.library.chocolate.dialog.AxBasicDialogFragment.UiModel(
        dialogType = com.mojise.library.chocolate.dialog.AxBasicDialogFragment.Type.OneButton,
        title = title ?: "알림",
        content = content,
        positiveButtonText = confirmButtonText ?: "확인",
        negativeButtonText = "",
    )
    return com.mojise.library.chocolate.dialog.AxBasicDialogLazy(dialogUiModel)
}

fun Fragment.axBasicTwoButtonDialog(
    title: String? = null,
    content: String,
    confirmButtonText: String? = null,
): Lazy<com.mojise.library.chocolate.dialog.AxBasicDialogFragment> {
    val dialogUiModel = com.mojise.library.chocolate.dialog.AxBasicDialogFragment.UiModel(
        dialogType = com.mojise.library.chocolate.dialog.AxBasicDialogFragment.Type.OneButton,
        title = title ?: "알림",
        content = content,
        positiveButtonText = confirmButtonText ?: "확인",
        negativeButtonText = "",
    )
    return com.mojise.library.chocolate.dialog.AxBasicDialogLazy(dialogUiModel)
}