package com.mojise.library.chocolate.bottomsheet

import android.app.Dialog
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.MATCH_PARENT
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.PARENT_ID
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.WRAP_CONTENT
import androidx.constraintlayout.widget.Guideline
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.isVisible
import androidx.core.view.setPadding
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mojise.library.chocolate.R
import com.mojise.library.chocolate.app.getChocolateThemeColor
import com.mojise.library.chocolate.ext.dp
import com.mojise.library.chocolate.ext.toRoundedBitmapDrawable
import com.mojise.library.chocolate.ext.toRoundedCornerDrawable

/**
 * ## BottomSheet Dialog Fragment with ViewDataBinding.
 *
 * - [BottomSheetDialogFragment]를 상속 받은 클래스.
 * - BottomSheet 다이얼로그 배경과 화면 사이에 Margin을 주어 간격이 떨어져 있는 디자인.
 * - 둥근 코너 디자인.
 * - BottomSheet 상단에 GripBar 디자인.
 *
 * ### 사용 예시 (Kotlin)
 * ```kotlin
 * class SimpleBottomSheet : ChocolateBottomSheetDialogFragment<XXBinding>() {
 *
 *     // Required Properties
 *     override val layoutResId: Int = R.layout.xx                  // ViewDataBinding Layout Resource ID
 *
 *     // Optional Properties
 *     // override val isOuterMarginEnabled: Boolean = true         // 외부 마진 사용 여부
 *     // override val outerMargin: Int = 12.dp                     // 외부 마진
 *     // override val cornerRadius: Int = 24.dp                    // 코너 라운딩 크기
 *     // override val isGripBarVisible: Boolean = true             // GripBar 표시 여부
 *     // override val gripBarColorResId: Int = R.color.xx          // GripBar 색상
 *     // override val backgroundColorResId: Int = R.color.xx       // 배경 색상
 *     // override val backgroundDrawableResId: Int = R.drawable.xx // 배경 Drawable
 *
 *     override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
 *         super.onViewCreated(view, savedInstanceState)
 *         // init view
 *     }
 * }
 * ```
 *
 * @property layoutResId ViewDataBinding Layout Resource ID
 * @property isOuterMarginEnabled BottomSheet Outer Margins 사용 여부 (default=true)
 * @property outerMargin BottomSheet Outer Margins (단위:px. default="10dp")
 * @property cornerRadius BottomSheet Corner Radius Size. (단위:Px. default="24dp", "16dp" or "24dp" 추천)
 * @property isGripBarVisible GripBar 표시 여부. (default=true)
 * @property gripBarColorResId GripBar의 색상 Resource ID
 * @property backgroundColorResId 바텀시트 배경 색상 Resource ID
 * @property backgroundDrawableResId 바텀시트 배경 Drawable Resource ID
 * @property innerContentPaddingVertical Vertical(Top & Bottom) Content Padding in BottomSheet (read-only)
 *
 * @see [BottomSheetDialogFragment]
 */
abstract class ChocolateBottomSheetDialogFragment<T : ViewDataBinding> : BottomSheetDialogFragment() {

    @get:LayoutRes
    protected abstract val layoutResId: Int

    /** BottomSheet Outer Margins 사용 여부 (default=true) */
    protected open val isOuterMarginEnabled: Boolean = true

    /** BottomSheet Outer Margins (단위:px. default="10dp") */
    protected open val outerMargin: Int = 10.dp

    /** BottomSheet Corner Radius Size (단위:Px. default="24dp", "16dp" or "24dp" 추천) */
    protected open val cornerRadius: Int = 24.dp

    /** 상단 회색 가로 줄의 GripBar 표시 여부 (false 일 경우, 상/하단의 Padding([innerContentPaddingVertical]) 영역도 같이 없어짐) */
    protected open val isGripBarVisible: Boolean = true

    /** GripBar의 색상 Resource ID */
    @ColorRes
    protected open val gripBarColorResId: Int = R.color.chocolate_bottom_sheet_grip_bar

    /** 바텀시트 배경 색상 Resource ID */
    @ColorRes
    protected open val backgroundColorResId: Int = R.color.chocolate_bottom_sheet_background

    /** 바텀시트 배경 Drawable Resource ID */
    @DrawableRes
    protected open val backgroundDrawableResId: Int? = null

    /** Vertical(Top & Bottom) Content Padding in BottomSheet */
    protected val innerContentPaddingVertical: Int
        get() = cornerRadius

    protected lateinit var binding: T
        private set

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
        //return BottomSheetDialog(requireContext(), com.google.android.material.R.style.Theme_Material3_DayNight_BottomSheetDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val contentRootLayout = inflater.inflate(R.layout.fragment_base_chocolate_floating_bottom_sheet, container, false) as ConstraintLayout
        contentRootLayout.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                updateBottomSheetDesign()
                contentRootLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })

        binding = DataBindingUtil.inflate(inflater, layoutResId, null, false)
        binding.lifecycleOwner = viewLifecycleOwner

        val gripBar = contentRootLayout.findViewById<View>(R.id.grip_bar)
        val contentPaddingTopGuideline = contentRootLayout.findViewById<Guideline>(R.id.guideline_content_padding_top)
        val contentPaddingBottomGuideline = contentRootLayout.findViewById<Guideline>(R.id.guideline_content_padding_bottom)

        val gripBarColor = getChocolateThemeColor(R.color.chocolate_bottom_sheet_grip_bar)
        gripBar.backgroundTintList = ColorStateList.valueOf(gripBarColor)
        gripBar.isVisible = isGripBarVisible

        if (isGripBarVisible) {
            contentPaddingTopGuideline.setGuidelineBegin(innerContentPaddingVertical)
            contentPaddingBottomGuideline.setGuidelineEnd(innerContentPaddingVertical)
        } else {
            // GripBar를 사용하지 않을 경우, Top & Bottom Content Padding도 0으로 설정
            contentPaddingTopGuideline.setGuidelineBegin(0)

            if (isOuterMarginEnabled) {
                contentPaddingBottomGuideline.setGuidelineEnd(0)
            }
        }

        contentRootLayout.addView(binding.root, ConstraintLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).also { params ->
            params.topToTop = contentPaddingTopGuideline.id
            params.startToStart = PARENT_ID
            params.endToEnd = PARENT_ID
            params.bottomToBottom = contentPaddingBottomGuideline.id
        })

        //updateBottomSheetBehavior()
        updateNavigationBarColor()
        updateBottomSheetBackground(contentRootLayout)

        return contentRootLayout
    }

    fun setOnDismissListener(listener: DialogInterface.OnDismissListener) {
        dialog?.setOnDismissListener(listener)
    }

    private fun updateBottomSheetDesign() {
        dialog?.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)?.let {
            it.setBackgroundColor(Color.TRANSPARENT)

            if (isOuterMarginEnabled) {
                it.setPadding(outerMargin)
            } else {
                it.setPadding(0)
            }
        }
    }

    private fun updateBottomSheetBackground(contentRootLayout: View) {
        if (backgroundDrawableResId == null) {
            contentRootLayout.background = GradientDrawable().also { drawable ->
                drawable.shape = GradientDrawable.RECTANGLE
                drawable.setColor(getChocolateThemeColor(backgroundColorResId))

                if (isOuterMarginEnabled) {
                    drawable.cornerRadius = cornerRadius.toFloat()
                } else {
                    drawable.cornerRadii = floatArrayOf(
                        cornerRadius.toFloat(), cornerRadius.toFloat(), // Top-left
                        cornerRadius.toFloat(), cornerRadius.toFloat(), // Top-right
                        0f, 0f, // Bottom-right
                        0f, 0f // Bottom-left
                    )
                }
            }
        } else {
            val drawable = ResourcesCompat.getDrawable(resources, backgroundDrawableResId!!, requireContext().theme)
            contentRootLayout.background = if (drawable is BitmapDrawable) {
                if (isOuterMarginEnabled) {
                    drawable.toRoundedBitmapDrawable(requireContext(), cornerRadius.toFloat())
                } else {
                    drawable.toRoundedCornerDrawable(requireContext(), cornerRadius.toFloat(), cornerRadius.toFloat(), 0f, 0f)
                }
            } else {
                drawable
            }
        }
    }

    private fun updateNavigationBarColor() {
        if (isOuterMarginEnabled) {
            dialog?.window?.let {
                it.navigationBarColor = getChocolateThemeColor(R.color.chocolate_modal_bottom_sheet_outside_color)
                WindowInsetsControllerCompat(it, it.decorView).isAppearanceLightNavigationBars = false
            }
        }
    }

    private fun updateBottomSheetBehavior() {
        val bottomSheet = dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) ?: return
        val behavior = BottomSheetBehavior.from(bottomSheet)

        behavior.skipCollapsed = true
    }
}