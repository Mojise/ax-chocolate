package com.mojise.library.chocolate.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.BundleCompat
import androidx.core.text.HtmlCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.mojise.library.chocolate.R
import com.mojise.library.chocolate.ext.dp
import java.io.Serializable

class AxBasicDialogFragment : DialogFragment() {

    private lateinit var binding: ViewBinding
    private lateinit var model: UiModel

    private var negativeButtonClickedAction: ClickAction = ClickAction()
    private var positiveButtonClickedAction: ClickAction = ClickAction()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = arguments
            ?.let { BundleCompat.getSerializable(it, PARAMS_DATA, UiModel::class.java) }
            ?: run {
                dismiss()
                return
            }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).also {
            it.requestWindowFeature(Window.FEATURE_NO_TITLE)
            it.setCanceledOnTouchOutside(false)
            // it.setCancelable(false)

            it.window?.setLayout(272.dp, ViewGroup.LayoutParams.WRAP_CONTENT)
            it.window?.setBackgroundDrawableResource(android.R.color.transparent)
            it.window?.setDimAmount(0.3f)

            it.setOnKeyListener { dialog, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (model.dialogType == Type.OneButton) {
                        return@setOnKeyListener true
                    }

                    negativeButtonClickedAction.action()

                    if (negativeButtonClickedAction.isAutoDismiss) {
                        dismiss()
                    }
                    true
                } else {
                    false
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.chocolate_dialog_basic, container, false).also {
        binding = ViewBinding(
            model = model,
            content = it.findViewById(R.id.content),
            btnNegative = it.findViewById(R.id.btn_negative),
            btnPositive = it.findViewById(R.id.btn_positive),
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.window?.setLayout(272.dp, ViewGroup.LayoutParams.WRAP_CONTENT)

        // 3binding.model = model
        binding.content.text = HtmlCompat.fromHtml(model.content, HtmlCompat.FROM_HTML_MODE_LEGACY)

        binding.btnNegative.setOnClickListener {
            negativeButtonClickedAction.action()

            if (negativeButtonClickedAction.isAutoDismiss) {
                dismiss()
            }
        }
        binding.btnPositive.setOnClickListener {
            positiveButtonClickedAction.action()

            if (positiveButtonClickedAction.isAutoDismiss) {
                dismiss()
            }
        }
    }

    fun from(supportFragmentManager: FragmentManager): TwoButtonRequestManager {
        return TwoButtonRequestManager(
            this,
            supportFragmentManager
        )
    }

//    fun from(supportFragmentManager: FragmentManager): OneButtonRequestManager {
//        return OneButtonRequestManager(this, supportFragmentManager)
//    }

    enum class Type {
        OneButton, TwoButton,
    }

    data class UiModel(
        val dialogType: Type,
        val title: String,
        val content: String,
        val negativeButtonText: String,
        val positiveButtonText: String,
    ) : Serializable

    data class ClickAction(
        val isAutoDismiss: Boolean = true,
        val action: () -> Unit = {},
    )

    class OneButtonRequestManager(
        private val dialog: AxBasicDialogFragment,
        private val supportFragmentManager: FragmentManager,
    ) {
        fun confirmClicked(isAutoDismiss: Boolean = true, action: () -> Unit) = apply {
            dialog.positiveButtonClickedAction =
                ClickAction(
                    isAutoDismiss = isAutoDismiss,
                    action = action
                )
        }

        fun show() = dialog.show(supportFragmentManager,
            TAG
        )
    }

    class TwoButtonRequestManager(
        private val dialog: AxBasicDialogFragment,
        private val supportFragmentManager: FragmentManager,
    ) {
        fun negativeClicked(isAutoDismiss: Boolean = true, action: () -> Unit) = apply {
            dialog.negativeButtonClickedAction =
                ClickAction(
                    isAutoDismiss = isAutoDismiss,
                    action = action
                )
        }

        fun positiveClicked(isAutoDismiss: Boolean = true, action: () -> Unit) = apply {
            dialog.positiveButtonClickedAction =
                ClickAction(
                    isAutoDismiss = isAutoDismiss,
                    action = action
                )
        }

        fun show() = dialog.show(supportFragmentManager,
            TAG
        )
    }

    private data class ViewBinding(
        val model: UiModel,
        val content: TextView,
        val btnNegative: TextView,
        val btnPositive: ConstraintLayout,
    )

    companion object {
        const val TAG = "AxBasicDialog"
        private const val PARAMS_DATA = "ax_dialog_data"

        fun newInstance(model: UiModel) = AxBasicDialogFragment()
            .also {
            it.arguments = Bundle().apply {
                putSerializable(PARAMS_DATA, model)
            }
        }
    }
}