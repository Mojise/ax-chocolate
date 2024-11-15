package com.mojise.library.chocolate.bottomsheet

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.setPadding
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mojise.library.chocolate.R
import com.mojise.library.chocolate.ext.dp

abstract class FloatingBottomSheetFragment<T : ViewDataBinding> : BottomSheetDialogFragment() {

    @get:LayoutRes
    protected abstract val layoutResId: Int
    protected open val outerMargin: Int = MARGIN_DP_DEFAULT // dp value
    protected open val cornerRadius: Int = RADIUS_DP_DEFAULT // dp value
    protected open val backgroundColor: Int = Color.WHITE
    protected open val backgroundDrawable: Drawable? = null

    protected lateinit var binding: T
        private set

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, layoutResId, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        binding.root.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                updateBottomSheetDesign()
                binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })

        //updateBottomSheetBehavior()
        updateNavigationBarColor()
        updateBottomSheetBackground()

        return binding.root
    }

    fun setOnDismissListener(listener: DialogInterface.OnDismissListener) {
        dialog?.setOnDismissListener(listener)
    }

    private fun updateBottomSheetDesign() {
        dialog?.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)?.let {
            it.setBackgroundColor(Color.TRANSPARENT)
            it.setPadding(outerMargin.dp)
        }
    }

    private fun updateBottomSheetBackground() {
        binding.root.background = backgroundDrawable ?: GradientDrawable().also { drawable ->
            drawable.shape = GradientDrawable.RECTANGLE
            drawable.cornerRadius = cornerRadius.dp.toFloat()
            drawable.setColor(backgroundColor)
        }
    }

    private fun updateNavigationBarColor() {
        dialog?.window?.let {
            it.navigationBarColor = requireContext().getColor(R.color.chocolate_modal_bottom_sheet_outside_color)

            WindowInsetsControllerCompat(it, it.decorView).isAppearanceLightNavigationBars = false
        }
    }

    private fun updateBottomSheetBehavior() {
        val bottomSheet = dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) ?: return
        val behavior = BottomSheetBehavior.from(bottomSheet)

        behavior.skipCollapsed = true
    }

    companion object {
        const val MARGIN_DP_DEFAULT = 12
        const val RADIUS_DP_DEFAULT = 24
    }
}