package com.mojise.library.chocolate.button.box

import android.animation.AnimatorSet
import android.animation.LayoutTransition
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.animation.doOnEnd
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.core.view.setPadding
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.mojise.library.chocolate.R
import com.mojise.library.chocolate.app.chocolateButtonBackgroundDisabledColor
import com.mojise.library.chocolate.app.chocolateButtonTextColor
import com.mojise.library.chocolate.app.chocolateButtonTextDisabledColor
import com.mojise.library.chocolate.app.primaryColor
import com.mojise.library.chocolate.ext.dp
import com.mojise.library.chocolate.ext.sp
import com.mojise.library.chocolate.util.TAG
import com.mojise.library.chocolate.view.ChocolateConstraintLayout
import com.mojise.library.chocolate.view.helper.ChocolateViewAttributesUtil
import com.mojise.library.chocolate.view.helper.ChocolateViewHelper
import com.mojise.library.chocolate.view.helper.ChocolateViewHelper.NO_COLOR
import com.mojise.library.chocolate.view.helper.setPaddingBottom
import com.mojise.library.chocolate.view.helper.setPaddingTop
import com.mojise.library.chocolate.view.model.ChocolateColorState
import com.mojise.library.chocolate.view.model.ChocolateTextStyle
import com.mojise.library.chocolate.view.model.PressEffectStrength

open class ChocolateBoxButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ChocolateConstraintLayout(context, attrs, defStyleAttr) {

    /**
     * ### 로딩 상태 여부
     *
     * - 로딩 중일 때는 버튼을 비활성화하고, 로딩 중을 나타내는 프로그레스 바를 표시.
     */
    var isLoading: Boolean = false
        set(loading) {
            field = loading
            binding.loading.isVisible = loading
            isEnabled = loading.not()
        }

    val isLayoutTransitionEnabled: Boolean
        get() = layoutTransition != null

    protected val binding: ViewBinding

    protected val boxButtonAttributes = ChocolateBoxButtonAttributes()

    private var animatorSet: AnimatorSet? = null

    init {
        //Log.e(TAG, "ChocolateBoxButton init")
        val view = inflate(context, R.layout.chocolate_box_button, this)

        binding = ViewBinding(
            iconLeft = view.findViewById(R.id.chocolate_box_button_icon_left),
            textView = view.findViewById(R.id.chocolate_box_button_text),
            iconRight = view.findViewById(R.id.chocolate_box_button_icon_right),
            loading = view.findViewById(R.id.chocolate_box_button_loading),
        )

        try {
            // Android Padding 속성 가져오기
            ChocolateViewAttributesUtil.obtainAndroidPaddings(
                context = context,
                attrs = attrs,
                defaultVerticalPadding = 14.dp,
                defaultHorizontalPadding = 28.dp,
            ).let { padding ->
                setPadding(padding.start, padding.top, padding.end, padding.bottom)
            }

            // Android 속성 가져오기
            context.obtainStyledAttributes(attrs, intArrayOf(
                android.R.attr.animateLayoutChanges,
            ), defStyleAttr, 0).use { array ->
                val animateLayoutChanges = array.getBoolean(0, true)
                layoutTransition = if (animateLayoutChanges) {
                    LayoutTransition().apply {
                        setInterpolator(LayoutTransition.APPEARING, DecelerateInterpolator())
                        setInterpolator(LayoutTransition.DISAPPEARING, DecelerateInterpolator())
                        setInterpolator(LayoutTransition.CHANGING, DecelerateInterpolator())
                        setInterpolator(LayoutTransition.CHANGE_APPEARING, DecelerateInterpolator())
                        setInterpolator(LayoutTransition.CHANGE_DISAPPEARING, DecelerateInterpolator())
                    }
                } else {
                    null
                }
            }
            // ChocolateBoxButton 속성 가져오기
            context.obtainStyledAttributes(attrs, R.styleable.ChocolateBoxButton).use { array ->
                array.getString(R.styleable.ChocolateBoxButton_chocolate_BoxButton_Text)
                    ?.let(binding.textView::setText)

                boxButtonAttributes.textSize = array.getDimension(R.styleable.ChocolateBoxButton_chocolate_BoxButton_TextSize, 18f.sp)
                boxButtonAttributes.textColor = array.getColor(R.styleable.ChocolateBoxButton_chocolate_BoxButton_TextColor, chocolateButtonTextColor)

                // FontFamily 설정
                array.getResourceId(R.styleable.ChocolateBoxButton_chocolate_BoxButton_FontFamily, NO_ID)
                    .takeIf { it != NO_ID }
                    ?.let { resId ->
                        binding.textView.typeface = ResourcesCompat.getFont(context, resId)
                    }

                val textStyle = array.getInt(R.styleable.ChocolateBoxButton_chocolate_BoxButton_TextStyle, ChocolateTextStyle.Normal.value)
                    .let(ChocolateTextStyle::valueOf)

                // Typeface(TextStyle) 설정
                binding.textView.typeface = when (textStyle) {
                    ChocolateTextStyle.Normal -> Typeface.create(binding.textView.typeface, Typeface.NORMAL)
                    ChocolateTextStyle.Bold -> Typeface.create(binding.textView.typeface, Typeface.BOLD)
                    ChocolateTextStyle.Italic -> Typeface.create(binding.textView.typeface, Typeface.ITALIC)
                    ChocolateTextStyle.BoldItalic -> Typeface.create(binding.textView.typeface, Typeface.BOLD_ITALIC)
                }

                // TextView Padding(Top/Bottom) 설정
                array.getDimension(R.styleable.ChocolateBoxButton_chocolate_BoxButton_TextPaddingTop, 0f)
                    .let(Float::toInt)
                    .let(binding.textView::setPaddingTop)
                array.getDimension(R.styleable.ChocolateBoxButton_chocolate_BoxButton_TextPaddingBottom, 0f)
                    .let(Float::toInt)
                    .let(binding.textView::setPaddingBottom)

                boxButtonAttributes.leftIconDrawable = array.getDrawable(R.styleable.ChocolateBoxButton_chocolate_BoxButton_LeftIconDrawable)
                boxButtonAttributes.leftIconTint = array.getColor(R.styleable.ChocolateBoxButton_chocolate_BoxButton_LeftIconTint, Color.WHITE)
                boxButtonAttributes.leftIconPadding = array.getDimension(R.styleable.ChocolateBoxButton_chocolate_BoxButton_LeftIconPadding, 0f)
                boxButtonAttributes.leftIconMarginWithText = array.getDimension(R.styleable.ChocolateBoxButton_chocolate_BoxButton_LeftIconMarginWithText, 4f.dp)

                boxButtonAttributes.rightIconDrawable = array.getDrawable(R.styleable.ChocolateBoxButton_chocolate_BoxButton_RightIconDrawable)
                boxButtonAttributes.rightIconTint = array.getColor(R.styleable.ChocolateBoxButton_chocolate_BoxButton_RightIconTint, Color.WHITE)
                boxButtonAttributes.rightIconPadding = array.getDimension(R.styleable.ChocolateBoxButton_chocolate_BoxButton_RightIconPadding, 0f)
                boxButtonAttributes.rightIconMarginWithText = array.getDimension(R.styleable.ChocolateBoxButton_chocolate_BoxButton_RightIconMarginWithText, 4f.dp)
            }

            binding.textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, boxButtonAttributes.textSize)
            binding.textView.setTextColor(boxButtonAttributes.textColor)

            boxButtonAttributes.leftIconDrawable?.let { leftIconDrawable ->
                binding.iconLeft.setImageDrawable(leftIconDrawable)
                binding.iconLeft.setPadding(boxButtonAttributes.leftIconPadding.toInt())
                boxButtonAttributes.leftIconTint?.let(binding.iconLeft::setColorFilter)
                binding.iconLeft.isVisible = true
            }

            boxButtonAttributes.rightIconDrawable?.let { rightIconDrawable ->
                binding.iconRight.setImageDrawable(rightIconDrawable)
                binding.iconRight.setPadding(boxButtonAttributes.rightIconPadding.toInt())
                boxButtonAttributes.rightIconTint?.let(binding.iconRight::setColorFilter)
                binding.iconRight.isVisible = true
            }

            // Chocolate 속성 가져오기
            context.obtainStyledAttributes(attrs, R.styleable.chocolateViewAttributes).use { array ->
                attributes.chocolate.backgroundColors = ChocolateColorState().apply {
                    enabledColor = array.getColor(R.styleable.chocolateViewAttributes_chocolate_BackgroundColor, primaryColor)
                    selectedColor = array.getColor(R.styleable.chocolateViewAttributes_chocolate_BackgroundSelectedColor, NO_COLOR)
                        .takeIf { it != NO_COLOR }
                        ?: enabledColor
                    disabledColor = array.getColor(R.styleable.chocolateViewAttributes_chocolate_BackgroundDisabledColor, chocolateButtonBackgroundDisabledColor)
                }

                val pressEffectStrengthInt = array.getInt(R.styleable.chocolateViewAttributes_chocolate_PressEffectStrengthLevel, -1)
                val pressEffectStrength = PressEffectStrength.valueOf(pressEffectStrengthInt)
                val pressEffectScaleRatio = pressEffectStrength?.value
                    ?: array.getFloat(R.styleable.chocolateViewAttributes_chocolate_PressEffectScaleRatio, PressEffectStrength.LightTiny.value)

                attributes.chocolate.pressEffectScaleRatio = pressEffectScaleRatio
            }

            binding.loading.setIndicatorColor(
                attributes.chocolate.backgroundColors.enabledColor
                    ?: primaryColor
            )
        } catch (e: Exception) {
            Log.w(TAG, "ChocolateBoxButton error", e)
        }

        ChocolateViewHelper.setRippleBackgroundOrForeground(this, attributes)
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        animateButtonStateChange(enabled)
    }

    /**
     * 버튼의 [enabled] 상태 변경 시 배경 색상, 텍스트 색상, 아이콘 색상을 변경하는 애니메이션을 실행.
     */
    private fun animateButtonStateChange(enabled: Boolean) {
        // 시작 배경 색상
        val currentBackgroundColor = (background as? ColorDrawable)?.color
            ?: attributes.chocolate.backgroundColors.disabledColor
            ?: Color.TRANSPARENT

        // 목표 배경 색상
        val targetBackgroundColor = if (enabled)
            attributes.chocolate.backgroundColors.enabledColor ?: Color.TRANSPARENT
            else attributes.chocolate.backgroundColors.disabledColor ?: Color.TRANSPARENT

        // 배경 색상 변경 애니메이션
        val backgroundAnimator = ValueAnimator.ofArgb(currentBackgroundColor, targetBackgroundColor).also {
            it.duration = if (isLayoutTransitionEnabled) ANIMATION_DURATION else 0
            it.interpolator = DecelerateInterpolator()
            it.addUpdateListener { animation ->
                val animatedValue = animation.animatedValue as Int
                backgroundTintList = ColorStateList.valueOf(animatedValue)
            }
        }
        // 시작 텍스트 색상
        val startTextColor =
            if (enabled) chocolateButtonTextDisabledColor
            else boxButtonAttributes.textColor
        // 목표 텍스트 색상
        val targetTextColor =
            if (enabled) boxButtonAttributes.textColor
            else chocolateButtonTextDisabledColor
        // 텍스트 색상 변경 애니메이션
        val textColorAnimator = ValueAnimator.ofArgb(startTextColor, targetTextColor).also {
            it.duration = if (isLayoutTransitionEnabled) ANIMATION_DURATION else 0
            it.interpolator = DecelerateInterpolator(2.5f)
            it.addUpdateListener { animation ->
                val animatedValue = animation.animatedValue as Int
//                if (enabled) Log.d(TAG, "animatedValue: $animatedValue")
//                else Log.w(TAG, "animatedValue: $animatedValue")
                binding.textView.setTextColor(animatedValue)
            }
            it.doOnEnd {
                binding.textView.setTextColor(targetTextColor)
            }
        }
        // 시작 Left 아이콘 색상
        val startIconLeftColor =
            if (enabled) chocolateButtonTextDisabledColor
            else boxButtonAttributes.leftIconTint ?: Color.TRANSPARENT
        // 목표 Left 아이콘 색상
        val targetIconLeftColor =
            if (enabled) boxButtonAttributes.leftIconTint ?: Color.TRANSPARENT
            else chocolateButtonTextDisabledColor
        // 아이콘 색상 변경 애니메이션
        val iconLeftColorAnimator = ValueAnimator.ofArgb(startIconLeftColor, targetIconLeftColor).also {
            it.duration = if (isLayoutTransitionEnabled) ANIMATION_DURATION else 0
            it.interpolator = DecelerateInterpolator(2.5f)
            it.addUpdateListener { animation ->
                val animatedValue = animation.animatedValue as Int
                binding.iconLeft.setColorFilter(animatedValue)
            }
            it.doOnEnd {
                binding.iconLeft.setColorFilter(targetIconLeftColor)
            }
        }

        // 시작 Right 아이콘 색상
        val startIconRightColor =
            if (enabled) chocolateButtonTextDisabledColor
            else boxButtonAttributes.rightIconTint ?: Color.TRANSPARENT
        // 목표 Right 아이콘 색상
        val targetIconRightColor =
            if (enabled) boxButtonAttributes.rightIconTint ?: Color.TRANSPARENT
            else chocolateButtonTextDisabledColor
        // 아이콘 색상 변경 애니메이션
        val iconRightColorAnimator = ValueAnimator.ofArgb(startIconRightColor, targetIconRightColor).also {
            it.duration = if (isLayoutTransitionEnabled) ANIMATION_DURATION else 0
            it.interpolator = DecelerateInterpolator(2.5f)
            it.addUpdateListener { animation ->
                val animatedValue = animation.animatedValue as Int
                binding.iconRight.setColorFilter(animatedValue)
            }
            it.doOnEnd {
                binding.iconRight.setColorFilter(targetIconRightColor)
            }
        }

        // 애니메이션 실행
        if (animatorSet?.isRunning == true) {
            // 아직 이전의 애니메이션이 실행 중일 때, 이전의 애니메이션을 중지한 뒤 새로운 애니메이션을 실행
            animatorSet?.doOnEnd {
                animatorSet = AnimatorSet().apply {
                    playTogether(backgroundAnimator, textColorAnimator, iconLeftColorAnimator, iconRightColorAnimator)
                    start()
                }
            }
            animatorSet?.end()
        } else {
            animatorSet = AnimatorSet().apply {
                playTogether(backgroundAnimator, textColorAnimator, iconLeftColorAnimator, iconRightColorAnimator)
                start()
            }
        }
    }

    /**
     * - 무한 로딩을 방지하기 위한 함수 (권장되지 않는 방식).
     * - [isLoading]=true 로 설정하고, 지정된 시간([timeout]) 이후에 로딩 상태를 자동으로 해제합니다.
     * - 해당 View의 로딩 상태를 화면 UI의 상태와 함께 관리될 수 있도록 하는 것이 더 권장되는 방식으로, [isLoading] 속성을 사용하여 직접 로딩 상태를 설정/해제 하는 것을 권장함.
     *
     * @param timeout 로딩 상태 유지 시간 (기본값: 5000ms)
     * @param endAction 로딩 상태 해제 후 실행할 작업
     */
    @Deprecated(
        message = "해당 함수 대신 isLoading 속성을 통해 로딩 상태를 직접 설정/해제 하는 방식을 더 권장함.",
        replaceWith = ReplaceWith("isLoading = true or false"),
    )
    fun setLoadingWithTimeout(timeout: Long = 10000L, endAction: (() -> Unit)? = null) {
        isLoading = true
        postDelayed({
            isLoading = false
            endAction?.invoke()
        }, timeout)
    }

    protected data class ViewBinding(
        val iconLeft: AppCompatImageView,
        val textView: AppCompatTextView,
        val iconRight: AppCompatImageView,
        val loading: CircularProgressIndicator,
    )

    protected data class ChocolateBoxButtonAttributes(
        var textSize: Float = 18.sp,
        var textColor: Int = Color.WHITE,
        var leftIconDrawable: Drawable? = null,
        var leftIconTint: Int? = null,
        var leftIconPadding: Float = 0f,
        var leftIconMarginWithText: Float = 4f.dp,
        var rightIconDrawable: Drawable? = null,
        var rightIconTint: Int? = null,
        var rightIconPadding: Float = 0f,
        var rightIconMarginWithText: Float = 4f.dp,
    )

    companion object {
        private const val ANIMATION_DURATION = 300L
    }
}