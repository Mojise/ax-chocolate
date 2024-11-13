package com.mojise.library.ax_chocolate.loading.modal

import androidx.annotation.ColorRes
import androidx.annotation.MainThread
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity


class AxModalLoadingDialogFragmentLazy(
    private val fragmentActivity: FragmentActivity,
    @ColorRes private val loadingColorResId: Int?,
    @StringRes private val loadingMessageResId: Int?,
) : Lazy<AxModalLoadingDialog> {

    private var cached: AxModalLoadingDialog? = null

    override val value: AxModalLoadingDialog
        get() = cached ?: AxModalLoadingDialog(fragmentActivity, loadingColorResId, loadingMessageResId)
            .also { cached = it }

    override fun isInitialized(): Boolean = cached != null
}

@MainThread
fun FragmentActivity.axModalLoadingDialog(
    @ColorRes loadingColorResId: Int? = null,
    @StringRes loadingMessageResId: Int? = null,
) : Lazy<AxModalLoadingDialog> {
    return AxModalLoadingDialogFragmentLazy(this, loadingColorResId, loadingMessageResId)
}

class AxModalLoadingDialogFragmentLazyForFragment(
    private val fragment: Fragment,
    @ColorRes private val loadingColorResId: Int?,
    @StringRes private val loadingMessageResId: Int?,
) : Lazy<AxModalLoadingDialog> {

    private var cached: AxModalLoadingDialog? = null

    override val value: AxModalLoadingDialog
        get() = cached ?: AxModalLoadingDialog(fragment.requireActivity(), loadingColorResId, loadingMessageResId)
            .also { cached = it }

    override fun isInitialized(): Boolean = cached != null
}

@MainThread
fun Fragment.axModalLoadingDialog(
    @ColorRes loadingColorResId: Int? = null,
    @StringRes loadingMessageResId: Int? = null,
): Lazy<AxModalLoadingDialog> {
    return AxModalLoadingDialogFragmentLazyForFragment(this, loadingColorResId, loadingMessageResId)
}