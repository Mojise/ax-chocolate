package com.mojise.library.ax_chocolate.dialog

class AxBasicDialogLazy(
    private val dialogUiModel: AxBasicDialogFragment.UiModel,
) : Lazy<AxBasicDialogFragment> {

    private var cached: AxBasicDialogFragment? = null

    override val value: AxBasicDialogFragment
        get() = cached ?: AxBasicDialogFragment.newInstance(dialogUiModel)
            .also { cached = it }

    override fun isInitialized(): Boolean = (cached != null)
}