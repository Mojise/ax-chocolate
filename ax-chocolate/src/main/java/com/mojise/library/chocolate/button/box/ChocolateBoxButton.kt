package com.mojise.library.chocolate.button.box

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.doOnEnd
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.core.view.setPadding
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.mojise.library.chocolate.R
import com.mojise.library.chocolate._internal.TAG
import com.mojise.library.chocolate._internal.theme.*
import com.mojise.library.chocolate._internal.etc.DecelerateInterpolatorLayoutTransition
import com.mojise.library.chocolate.ext.getColorOrNull
import com.mojise.library.chocolate.ext.setPaddingBottom
import com.mojise.library.chocolate.ext.setPaddingTop
import com.mojise.library.chocolate.ext.dp
import com.mojise.library.chocolate.ext.useCompat
import com.mojise.library.chocolate.view.ChocolateView
import com.mojise.library.chocolate.view.helper.ChocolateViewHelper
import com.mojise.library.chocolate.view.model.ChocolateColorState
import com.mojise.library.chocolate.view.model.ChocolateTextStyle
import com.mojise.library.chocolate.view.model.DrawablePosition
import com.mojise.library.chocolate.view.model.PressEffectStrength
import com.mojise.library.chocolate.view.util.ChocolateViewAttributesUtil
import com.mojise.library.chocolate.view.util.ChocolateViewUtil

open class ChocolateBoxButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ChocolateView {

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

    /**
     * 터치 시 눌림 효과 사용 여부
     */
    override var isPressEffectEnabled: Boolean
        get() = attributes.isPressEffectEnabled
        set(value) { attributes.isPressEffectEnabled = value }

    private val isLayoutTransitionEnabled: Boolean
        get() = layoutTransition != null

    protected val binding: ViewBinding
    protected val attributes: ChocolateBoxButtonAttributes = ChocolateBoxButtonAttributes()

    private var animatorSet: AnimatorSet? = null
    /** android:background로 설정한 것인지, 초콜릿 버튼의 백그라운드 색상 설정 방식으로 설정한 것인지 여부 */
    private var isUserSetBackground = false

    init {
        val view = inflate(context, R.layout.chocolate_box_button, this)

        binding = ViewBinding(
            textView = view.findViewById(R.id.chocolate_box_button_text),
            iconLeft = view.findViewById(R.id.chocolate_box_button_icon_left),
            iconRight = view.findViewById(R.id.chocolate_box_button_icon_right),
            loading = view.findViewById(R.id.chocolate_box_button_loading),
        )

        // Android 속성 가져오기
        context.obtainStyledAttributes(attrs, intArrayOf(
            android.R.attr.clickable,
            android.R.attr.background,
            android.R.attr.animateLayoutChanges,
        ), defStyleAttr, 0).useCompat {
            isClickable = it.getBoolean(0, true)
            val androidBackground = it.getDrawable(1)
            if (androidBackground != null) {
                background = androidBackground
            }

            val isAnimateLayoutChanges = it.getBoolean(2, true)
            layoutTransition = if (isAnimateLayoutChanges) DecelerateInterpolatorLayoutTransition() else null
        }
        // Android Padding 속성 가져오기
        ChocolateViewAttributesUtil.obtainAndroidPaddings(
            context = context,
            attrs = attrs,
            defaultVerticalPadding = 14.dp,
            defaultHorizontalPadding = 28.dp,
        ).let { padding ->
            setPadding(padding.start, padding.top, padding.end, padding.bottom)
        }

        // ChocolateBoxButton 속성 가져오기
        context.obtainStyledAttributes(attrs, R.styleable.ChocolateBoxButton).useCompat { array ->
            attributes.isPressEffectEnabled = array.getBoolean(R.styleable.ChocolateBoxButton_chocolate_PressEffectEnabled, true)

            // 눌림 효과 속성
            val pressEffectStrengthInt = array.getInt(R.styleable.ChocolateBoxButton_chocolate_PressEffectStrengthLevel, theme_chocolate_press_effect_strength_level)
            val pressEffectStrength = PressEffectStrength.valueOf(pressEffectStrengthInt)
            val pressEffectScaleRatio = array.getFloat(R.styleable.ChocolateBoxButton_chocolate_PressEffectScaleRatio, -1f)
                .takeIf { it in 0f..1f }
                ?: pressEffectStrength.value

            attributes.pressEffectScaleRatio = pressEffectScaleRatio

            // 테두리 속성 (width > 0 이고, 색상이 하나 이상 지정된 경우에만 적용)
            val strokeWidth = array.getDimension(R.styleable.ChocolateBoxButton_chocolate_BoxButton_StrokeWidth, 0f)
            val strokeEnabledColor = array.getColorOrNull(R.styleable.ChocolateBoxButton_chocolate_BoxButton_StrokeColor)
            val strokeSelectedColor = array.getColorOrNull(R.styleable.ChocolateBoxButton_chocolate_BoxButton_StrokeSelectedColor)
            val strokeDisabledColor = array.getColorOrNull(R.styleable.ChocolateBoxButton_chocolate_BoxButton_StrokeDisabledColor)
            val strokeColors =
                if (strokeWidth > 0f && (strokeSelectedColor != null || strokeEnabledColor != null || strokeDisabledColor != null))
                    ChocolateColorState(
                        enabledColor = strokeEnabledColor ?: Color.TRANSPARENT,
                        selectedColor = strokeSelectedColor ?: strokeEnabledColor ?: Color.TRANSPARENT,
                        disabledColor = strokeDisabledColor ?: Color.TRANSPARENT,
                    )
                else null
            attributes.strokeWidth = strokeWidth
            attributes.strokeColors = strokeColors

            val enabledColor = array.getColor(R.styleable.ChocolateBoxButton_chocolate_BoxButton_RippleColor, theme_chocolate_box_button_ripple_color)
            val selectedColor = array.getColorOrNull(R.styleable.ChocolateBoxButton_chocolate_BoxButton_RippleSelectedColor)
                ?: enabledColor
            attributes.rippleColors = ChocolateColorState(
                enabledColor = enabledColor,
                selectedColor = selectedColor,
                disabledColor = Color.TRANSPARENT,
            )

            // 배경 색상 속성
            val backgroundCornerRadius = array.getDimension(R.styleable.ChocolateBoxButton_chocolate_BoxButton_BackgroundCornerRadius, theme_chocolate_box_button_background_corner_radius)
            val backgroundEnabledColor = array.getColor(R.styleable.ChocolateBoxButton_chocolate_BoxButton_BackgroundColor, theme_chocolate_box_button_background_color)
            val backgroundSelectedColor = array.getColorOrNull(R.styleable.ChocolateBoxButton_chocolate_BoxButton_BackgroundSelectedColor)
            val backgroundDisabledColor = array.getColor(R.styleable.ChocolateBoxButton_chocolate_BoxButton_BackgroundDisabledColor, theme_chocolate_box_button_background_disabled_color)
            attributes.backgroundCornerRadius = backgroundCornerRadius
            attributes.backgroundColors = ChocolateColorState(
                enabledColor = backgroundEnabledColor,
                selectedColor = backgroundSelectedColor ?: backgroundEnabledColor,
                disabledColor = backgroundDisabledColor,
            )

            foreground = ChocolateViewUtil.generateRippleDrawable(
                cornerRadius = attributes.backgroundCornerRadius,
                rippleColors = attributes.rippleColors,
                strokeWidth = attributes.strokeWidth.toInt(),
                strokeColors = attributes.strokeColors,
                backgroundColors = null,
            )
            if (isUserSetBackground.not()) {
                setChocolateBackground(attributes)
            }

            // 텍스트 속성
            array.getString(R.styleable.ChocolateBoxButton_chocolate_BoxButton_Text)
                ?.let(binding.textView::setText)
            attributes.textSize = array.getDimension(R.styleable.ChocolateBoxButton_chocolate_BoxButton_TextSize, theme_chocolate_box_button_text_size)
            attributes.textColor = array.getColor(R.styleable.ChocolateBoxButton_chocolate_BoxButton_TextColor, theme_chocolate_box_button_text_color)
            attributes.textDisabledColor = array.getColor(R.styleable.ChocolateBoxButton_chocolate_BoxButton_TextDisabledColor, theme_chocolate_box_button_text_disabled_color)
            val textStyle = array.getInt(R.styleable.ChocolateBoxButton_chocolate_BoxButton_TextStyle, theme_chocolate_box_button_text_style)
                .let(ChocolateTextStyle::valueOf)
            array.getResourceId(R.styleable.ChocolateBoxButton_chocolate_BoxButton_FontFamily, theme_chocolate_box_button_font_family_res_id_or_zero)
                .takeIf { it != 0 }
                ?.let { resId ->
                    binding.textView.typeface = ResourcesCompat.getFont(context, resId)
                }
            // 텍스트 Typeface(TextStyle) 설정
            binding.textView.typeface = when (textStyle) {
                ChocolateTextStyle.Normal -> Typeface.create(binding.textView.typeface, Typeface.NORMAL)
                ChocolateTextStyle.Bold -> Typeface.create(binding.textView.typeface, Typeface.BOLD)
                ChocolateTextStyle.Italic -> Typeface.create(binding.textView.typeface, Typeface.ITALIC)
                ChocolateTextStyle.BoldItalic -> Typeface.create(binding.textView.typeface, Typeface.BOLD_ITALIC)
            }
            // TextView Padding(Top/Bottom) 설정
            array.getDimension(R.styleable.ChocolateBoxButton_chocolate_BoxButton_TextPaddingTop, theme_chocolate_box_button_text_padding_top)
                .let(Float::toInt)
                .let(binding.textView::setPaddingTop)
            array.getDimension(R.styleable.ChocolateBoxButton_chocolate_BoxButton_TextPaddingBottom, theme_chocolate_box_button_text_padding_bottom)
                .let(Float::toInt)
                .let(binding.textView::setPaddingBottom)

            // Left 아이콘 속성
            attributes.leftIconDrawable = array.getDrawable(R.styleable.ChocolateBoxButton_chocolate_BoxButton_LeftIconDrawable)
            attributes.leftIconTint = array.getColorOrNull(R.styleable.ChocolateBoxButton_chocolate_BoxButton_LeftIconTint)
            attributes.leftIconPadding = array.getDimension(R.styleable.ChocolateBoxButton_chocolate_BoxButton_LeftIconPadding, theme_chocolate_box_button_left_icon_padding)
            attributes.leftIconMarginWithText = array.getDimension(R.styleable.ChocolateBoxButton_chocolate_BoxButton_LeftIconMarginWithText, theme_chocolate_box_button_left_icon_margin_with_text)

            // Right 아이콘 속성
            attributes.rightIconDrawable = array.getDrawable(R.styleable.ChocolateBoxButton_chocolate_BoxButton_RightIconDrawable)
            attributes.rightIconTint = array.getColorOrNull(R.styleable.ChocolateBoxButton_chocolate_BoxButton_RightIconTint)
            attributes.rightIconPadding = array.getDimension(R.styleable.ChocolateBoxButton_chocolate_BoxButton_RightIconPadding, theme_chocolate_box_button_right_icon_padding)
            attributes.rightIconMarginWithText = array.getDimension(R.styleable.ChocolateBoxButton_chocolate_BoxButton_RightIconMarginWithText, theme_chocolate_box_button_right_icon_margin_with_text)
        }

        binding.textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, attributes.textSize)
        binding.textView.setTextColor(attributes.textColor)

        attributes.leftIconDrawable?.let { leftIconDrawable ->
            binding.iconLeft.setImageDrawable(leftIconDrawable)
            binding.iconLeft.setPadding(attributes.leftIconPadding.toInt())
            attributes.leftIconTint?.let(binding.iconLeft::setColorFilter)
            binding.iconLeft.isVisible = true
        }

        attributes.rightIconDrawable?.let { rightIconDrawable ->
            binding.iconRight.setImageDrawable(rightIconDrawable)
            binding.iconRight.setPadding(attributes.rightIconPadding.toInt())
            attributes.rightIconTint?.let(binding.iconRight::setColorFilter)
            binding.iconRight.isVisible = true
        }

        // 로딩바 색상 설정
        binding.loading.setIndicatorColor(
            attributes.backgroundColors.enabledColor
        )
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean = try {
        if (isEnabled && isClickable && isPressEffectEnabled) {
            ChocolateViewHelper.animatePressEffectOnTouch(this, event, attributes.pressEffectScaleRatio)
        }
        super.dispatchTouchEvent(event)
    } catch (e: Exception) {
        // 터치 이벤트가 빠르게 연속적으로 발생하는 것을 방지하거나, 이미 파괴된 뷰에 이벤트가 전달되었을 경우 등을 위한 예외 처리
        Log.w(TAG, "dispatchTouchEvent error", e)
        false
    }

    override fun setEnabled(enabled: Boolean) {
        val prevEnabled = isEnabled
        super.setEnabled(enabled)

        if (prevEnabled != enabled) {
            animateButtonEnableStateColorChanged(enabled)
        }
    }

    override fun setBackground(background: Drawable?) {
        isUserSetBackground = true
        super.setBackground(background)
    }

    private fun setChocolateBackground(attributes: ChocolateBoxButtonAttributes) {
        isUserSetBackground = false

        val backgroundDrawable = ChocolateViewUtil.generateDrawable(
            cornerRadius = attributes.backgroundCornerRadius,
            strokeWidth = attributes.strokeWidth.toInt(),
            strokeColors = attributes.strokeColors,
            backgroundColors = attributes.backgroundColors,
        )
        super.setBackground(backgroundDrawable)
    }

    /**
     * 버튼의 [enabled] 상태 변경 시 배경 색상, 텍스트 색상, 아이콘 색상을 변경하는 애니메이션을 실행.
     */
    private fun animateButtonEnableStateColorChanged(enabled: Boolean) {
        if (isUserSetBackground) {
            return
        }

        val animators = mutableListOf<ValueAnimator>()

        // 시작 배경 색상
        val startBackgroundColor = if (enabled)
            attributes.backgroundColors.disabledColor
            else attributes.backgroundColors.enabledColor

        // 목표 배경 색상
        val targetBackgroundColor = if (enabled)
            attributes.backgroundColors.enabledColor
            else attributes.backgroundColors.disabledColor

        // 배경 색상 변경 애니메이션
        val backgroundAnimator = ValueAnimator.ofArgb(startBackgroundColor, targetBackgroundColor).also {
            it.duration = if (isLayoutTransitionEnabled) ANIMATION_DURATION else 0
            it.interpolator = DecelerateInterpolator()
            it.addUpdateListener { animation ->
                val animatedValue = animation.animatedValue as Int
                backgroundTintList = ColorStateList.valueOf(animatedValue)
            }
            it.doOnEnd {
                backgroundTintList = ColorStateList.valueOf(targetBackgroundColor)
            }
        }
        animators.add(backgroundAnimator)

        // 시작 텍스트 색상
        val startTextColor =
            if (enabled) attributes.textDisabledColor
            else attributes.textColor
        // 목표 텍스트 색상
        val targetTextColor =
            if (enabled) attributes.textColor
            else attributes.textDisabledColor
        // 텍스트 색상 변경 애니메이션
        val textColorAnimator = ValueAnimator.ofArgb(startTextColor, targetTextColor).also {
            it.duration = if (isLayoutTransitionEnabled) ANIMATION_DURATION else 0
            it.interpolator = DecelerateInterpolator(ANIMATION_FACTOR)
            it.addUpdateListener { animation ->
                val animatedValue = animation.animatedValue as Int
                binding.textView.setTextColor(animatedValue)
            }
            it.doOnEnd {
                binding.textView.setTextColor(targetTextColor)
            }
        }
        animators.add(textColorAnimator)

        if (binding.iconLeft.isVisible && binding.iconLeft.drawable != null) {
            // 시작 Left 아이콘 색상
            val startIconLeftColor =
                if (enabled) attributes.textDisabledColor
                else attributes.leftIconTint ?: Color.TRANSPARENT
            // 목표 Left 아이콘 색상
            val targetIconLeftColor =
                if (enabled) attributes.leftIconTint ?: Color.TRANSPARENT
                else attributes.textDisabledColor
            // 아이콘 색상 변경 애니메이션
            val iconLeftColorAnimator = ValueAnimator.ofArgb(startIconLeftColor, targetIconLeftColor).also {
                it.duration = if (isLayoutTransitionEnabled) ANIMATION_DURATION else 0
                it.interpolator = DecelerateInterpolator(ANIMATION_FACTOR)
                it.addUpdateListener { animation ->
                    val animatedValue = animation.animatedValue as Int
                    binding.iconLeft.setColorFilter(animatedValue)
                }
                it.doOnEnd {
                    binding.iconLeft.setColorFilter(targetIconLeftColor)
                }
            }
            animators.add(iconLeftColorAnimator)
        }

        if (binding.iconRight.isVisible && binding.iconRight.drawable != null) {
            // 시작 Right 아이콘 색상
            val startIconRightColor =
                if (enabled) attributes.textDisabledColor
                else attributes.rightIconTint ?: Color.TRANSPARENT
            // 목표 Right 아이콘 색상
            val targetIconRightColor =
                if (enabled) attributes.rightIconTint ?: Color.TRANSPARENT
                else attributes.textDisabledColor
            // 아이콘 색상 변경 애니메이션
            val iconRightColorAnimator = ValueAnimator.ofArgb(startIconRightColor, targetIconRightColor).also {
                it.duration = if (isLayoutTransitionEnabled) ANIMATION_DURATION else 0
                it.interpolator = DecelerateInterpolator(ANIMATION_FACTOR)
                it.addUpdateListener { animation ->
                    val animatedValue = animation.animatedValue as Int
                    binding.iconRight.setColorFilter(animatedValue)
                }
                it.doOnEnd {
                    binding.iconRight.setColorFilter(targetIconRightColor)
                }
            }
            animators.add(iconRightColorAnimator)
        }

        // 애니메이션 실행
        if (animatorSet?.isRunning == true) {
            // 아직 이전의 애니메이션이 실행 중일 때, 이전의 애니메이션을 중지한 뒤 새로운 애니메이션을 실행
            animatorSet?.doOnEnd {
                animatorSet = AnimatorSet().apply {
                    playTogether(animators.toList())
                    start()
                }
            }
            animatorSet?.end()
        } else {
            animatorSet = AnimatorSet().apply {
                playTogether(animators.toList())
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
        var isPressEffectEnabled: Boolean = true,
        var pressEffectScaleRatio: Float = 1f,

        var rippleColors: ChocolateColorState = ChocolateColorState.Transparent,
        var ripplePosition: DrawablePosition = DrawablePosition.Foreground,
        var strokeWidth: Float = 0f,
        var strokeColors: ChocolateColorState? = null,
        var backgroundColors: ChocolateColorState = ChocolateColorState.Transparent,
        var backgroundCornerRadius: Float = 0f,

        var textSize: Float = 0f,
        var textColor: Int = Color.TRANSPARENT,
        var textDisabledColor: Int = Color.TRANSPARENT,
        var leftIconDrawable: Drawable? = null,
        var leftIconTint: Int? = null,
        var leftIconPadding: Float = 0f,
        var leftIconMarginWithText: Float = 0f,
        var rightIconDrawable: Drawable? = null,
        var rightIconTint: Int? = null,
        var rightIconPadding: Float = 0f,
        var rightIconMarginWithText: Float = 0f,
    )

    companion object {
        private const val ANIMATION_DURATION = 180L
        private const val ANIMATION_FACTOR = 1f
    }
}