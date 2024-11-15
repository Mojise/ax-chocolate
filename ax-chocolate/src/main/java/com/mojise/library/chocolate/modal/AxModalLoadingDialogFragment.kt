package com.mojise.library.chocolate.modal

import android.app.Dialog
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.os.BundleCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.model.KeyPath
import com.mojise.library.chocolate.R
import com.mojise.library.chocolate.ext.getColorFromTheme
import com.mojise.library.chocolate.modal.customview.LoadingIndicatorView
import com.mojise.library.chocolate.util.TAG
import java.io.Serializable

class AxModalLoadingDialogFragment : DialogFragment() {

    private lateinit var binding: ViewBinding
    private var uiModel: UiModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        uiModel = arguments?.let {
            BundleCompat.getSerializable(it, ARGS_UI_MODEL, UiModel::class.java)
        }
    }

    override fun onCreateDialog(
        savedInstanceState: Bundle?
    ): Dialog = super.onCreateDialog(savedInstanceState).apply {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.chocolate_dialog_modal_loading)
        setCanceledOnTouchOutside(false)

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setDimAmount(0.35f)

        // Back key event is disabled
//        setOnKeyListener { _, keyCode, _ ->
//            keyCode == KeyEvent.KEYCODE_BACK
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.chocolate_dialog_modal_loading, container, false).apply {
        binding = ViewBinding(
            loadingIndicatorView = findViewById(R.id.loading_indicator_view),
            // loadingLottieView = findViewById(R.id.loading_lottie_view),
            loadingMessage = findViewById(R.id.loading_message),
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e(TAG, "onViewCreated")
        Log.e(TAG, "onViewCreated: uiModel=$uiModel")
        Log.e(TAG, "onViewCreated: AxModalLoadingDialog.GlobalOption.loadingMessageResId=${AxModalLoadingDialog.GlobalOption.loadingMessageResId}")

        val loadingColorResId = uiModel?.loadingColorResId
            ?: AxModalLoadingDialog.GlobalOption.loadingColorResId
        val loadingColor = loadingColorResId?.let {
            ContextCompat.getColor(requireContext(), it)
        } ?: requireContext().getColorFromTheme(android.R.attr.colorAccent)

        binding.loadingIndicatorView.setIndicatorColor(loadingColor)

        val loadingMessageResId = uiModel?.loadingMessageResId
            ?: AxModalLoadingDialog.GlobalOption.loadingMessageResId
        val loadingMessage = loadingMessageResId?.let {
            getString(it)
        } ?: getString(R.string.chocolate_modal_loading_message_data_loading)

        binding.loadingMessage.text = loadingMessage
    }

    fun show(supportFragmentManager: FragmentManager) {
//        if (isVisible.not()) {
//            show(supportFragmentManager, TAG)
//        }
        if (supportFragmentManager.findFragmentByTag(TAG_AX_MODAL_LOADING) == null) {
            show(supportFragmentManager, TAG_AX_MODAL_LOADING)
        }
    }

    fun hide() {
        if (isVisible) {
            dismiss()
        }
    }

    /**
     * sets tint for the animation.
     * @itemsToTint the items elements ("nm" inside "layers") the animation to tint.
     * null will reset all tinting. Empty will set tint for all.
     */
    private fun LottieAnimationView.setAnimationTint(itemsToTint: Array<String>?, @ColorInt color: Int) {
        //based on https://stackoverflow.com/a/45607292/878126 https://airbnb.io/lottie/#/android?id=dynamic-properties https://github.com/SolveSoul/lottie-android-colorfilter-sample
        if (itemsToTint == null) {
            //un-tint
            addValueCallback(KeyPath("**"), LottieProperty.COLOR_FILTER) { null }
            return
        }
        addValueCallback(
            KeyPath(*itemsToTint, "**"),
            LottieProperty.COLOR_FILTER
        ) { PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP) }
    }

    private data class ViewBinding(
        val loadingIndicatorView: com.mojise.library.chocolate.modal.customview.LoadingIndicatorView,
        // val loadingLottieView: LottieAnimationView,
        val loadingMessage: TextView,
    )

    private data class UiModel(
        val loadingStyle: AxModalLoadingStyle?,
        @ColorRes
        val loadingColorResId: Int?,
        @StringRes
        val loadingMessageResId: Int?,
    ) : Serializable

    companion object {
        const val TAG_AX_MODAL_LOADING = "AxModalLoadingDialogFragment"
        private const val ARGS_UI_MODEL = "uiModel"

        fun newInstance(
            loadingStyle: AxModalLoadingStyle?,
            @ColorRes loadingColorResId: Int?,
            @StringRes loadingMessageResId: Int?,
        ) = AxModalLoadingDialogFragment().apply {
            arguments = Bundle().apply {
                val uiModel = UiModel(
                    loadingStyle,
                    loadingColorResId,
                    loadingMessageResId,
                )
                putSerializable(ARGS_UI_MODEL, uiModel)
            }
        }
    }
}