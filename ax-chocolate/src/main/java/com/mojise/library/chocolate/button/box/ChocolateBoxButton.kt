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
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.MATCH_CONSTRAINT
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.PARENT_ID
import androidx.core.animation.doOnEnd
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.core.view.marginEnd
import androidx.core.view.marginStart
import androidx.core.view.setMargins
import androidx.core.view.setPadding
import androidx.core.view.updateLayoutParams
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.mojise.library.chocolate.R
import com.mojise.library.chocolate._internal.TAG
import com.mojise.library.chocolate._internal.etc.DecelerateInterpolatorLayoutTransition
import com.mojise.library.chocolate._internal.theme.theme_chocolate_box_button_background_color
import com.mojise.library.chocolate._internal.theme.theme_chocolate_box_button_background_corner_radius
import com.mojise.library.chocolate._internal.theme.theme_chocolate_box_button_background_disabled_color
import com.mojise.library.chocolate._internal.theme.theme_chocolate_box_button_box_padding_horizontal
import com.mojise.library.chocolate._internal.theme.theme_chocolate_box_button_box_padding_vertical
import com.mojise.library.chocolate._internal.theme.theme_chocolate_box_button_chain_style
import com.mojise.library.chocolate._internal.theme.theme_chocolate_box_button_font_family_res_id_or_zero
import com.mojise.library.chocolate._internal.theme.theme_chocolate_box_button_gravity
import com.mojise.library.chocolate._internal.theme.theme_chocolate_box_button_icon_margin_with_text
import com.mojise.library.chocolate._internal.theme.theme_chocolate_box_button_icon_padding
import com.mojise.library.chocolate._internal.theme.theme_chocolate_box_button_icon_position
import com.mojise.library.chocolate._internal.theme.theme_chocolate_box_button_ripple_color
import com.mojise.library.chocolate._internal.theme.theme_chocolate_box_button_text_color
import com.mojise.library.chocolate._internal.theme.theme_chocolate_box_button_text_disabled_color
import com.mojise.library.chocolate._internal.theme.theme_chocolate_box_button_text_padding_bottom
import com.mojise.library.chocolate._internal.theme.theme_chocolate_box_button_text_padding_top
import com.mojise.library.chocolate._internal.theme.theme_chocolate_box_button_text_size
import com.mojise.library.chocolate._internal.theme.theme_chocolate_box_button_text_style
import com.mojise.library.chocolate._internal.theme.theme_chocolate_press_effect_strength_level
import com.mojise.library.chocolate.ext.dp
import com.mojise.library.chocolate.ext.getColorOrNull
import com.mojise.library.chocolate.ext.getDimensionOrNull
import com.mojise.library.chocolate.ext.setPaddingBottom
import com.mojise.library.chocolate.ext.setPaddingTop
import com.mojise.library.chocolate.ext.useCompat
import com.mojise.library.chocolate.view.ChocolateView
import com.mojise.library.chocolate.view.helper.ChocolateViewHelper
import com.mojise.library.chocolate.view.model.ChocolateColorState
import com.mojise.library.chocolate.view.model.ChocolateTextStyle
import com.mojise.library.chocolate.view.model.DrawablePosition
import com.mojise.library.chocolate.view.model.PressEffectStrength
import com.mojise.library.chocolate.view.util.ChocolateViewAttributesUtil
import com.mojise.library.chocolate.view.util.ChocolateViewUtil
import kotlin.math.sqrt

class ChocolateBoxButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ChocolateView {

    /**
     * 터치 시 눌림 효과 사용 여부
     */
    override var isPressEffectEnabled: Boolean
        get() = attributes.isPressEffectEnabled
        set(value) { attributes.isPressEffectEnabled = value }

    /**
     * ### 로딩 상태 여부
     *
     * - 로딩 중일 때는 버튼을 비활성화하고, 로딩 중을 나타내는 프로그레스 바를 표시.
     */
    var isLoading: Boolean = false
        set(loading) {
            field = loading
            binding.loading.isVisible = loading
            binding.iconContainer.isVisible = if (loading) true else attributes.hasIconDrawable
            isEnabled = loading.not()
            animateLoading(loading)
        }

    /**
     * 버튼 텍스트 설정
     */
    var text: CharSequence
        get() = binding.textView.text
        set(value) {
            binding.textView.text = value
            binding.textView.isVisible = value.isNotBlank()
        }

    private val isLayoutTransitionEnabled: Boolean
        get() = layoutTransition != null

    private val binding: ViewBinding
    private val attributes: ChocolateBoxButtonAttributes = ChocolateBoxButtonAttributes()

    private var enabledAnimatorSet: AnimatorSet? = null
    private var loadingAnimatorSet: AnimatorSet? = null

    /** android:background로 설정한 것인지, 초콜릿 버튼의 백그라운드 색상 설정 방식으로 설정한 것인지 여부 */
    private var isUserSetBackground = false

    init {
        binding = ViewBinding(
            loading = CircularProgressIndicator(context, attrs, defStyleAttr).also {
                it.layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT).also { params ->
                    params.gravity = android.view.Gravity.CENTER
                    params.setMargins(0) // 이것도 필요함 (why...?)
                }
                it.setPadding(0) // 이것도 필요함 (why...?)
                it.id = R.id.chocolate_box_button_loading
                it.indicatorInset = 0
                it.trackThickness = 2.2f.dp.toInt()
                it.trackCornerRadius = 10.dp
                it.setIndicatorColor(theme_chocolate_box_button_text_color)
                it.isIndeterminate = true
                it.isVisible = false
            },
            icon = AppCompatImageView(context, attrs, defStyleAttr).also {
                it.layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT).also { params ->
                    params.gravity = android.view.Gravity.CENTER
                }
                it.id = R.id.chocolate_box_button_icon
                it.adjustViewBounds = true
                it.isVisible = false
            },
            iconContainer = FrameLayout(context, attrs, defStyleAttr).also {
                it.layoutParams = LayoutParams(MATCH_CONSTRAINT, MATCH_CONSTRAINT).also { params ->
                    params.matchConstraintPercentHeight = 1f
                    params.dimensionRatio = "1:1"
                    params.setMargins(0) // 이것도 필요함 (why...?)
                }
                it.id = R.id.chocolate_box_button_icon_container
                it.setPadding(0) // 이게 있어야 icon=null일 떄, loading이 보임 (원래 0인데 왜...?)
                it.isVisible = false
            },
            textView = AppCompatTextView(context, attrs, defStyleAttr).also {
                it.layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT).also { params ->
                    params.constrainedWidth = true
                    params.constrainedHeight = true
                    params.goneStartMargin = 0
                    params.goneEndMargin = 0
                }
                it.id = R.id.chocolate_box_button_text
                it.maxLines = 1
                it.ellipsize = android.text.TextUtils.TruncateAt.END
            },
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
            defaultVerticalPadding = theme_chocolate_box_button_box_padding_vertical,
            defaultHorizontalPadding = theme_chocolate_box_button_box_padding_horizontal,
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

            // 아이콘 및 텍스트 위치 관련 속성
            attributes.iconPosition = array.getInt(R.styleable.ChocolateBoxButton_chocolate_BoxButton_IconPosition, theme_chocolate_box_button_icon_position)
                .let(IconPosition::fromValue)
            attributes.chainStyle = array.getInt(R.styleable.ChocolateBoxButton_chocolate_BoxButton_ChainStyle, theme_chocolate_box_button_chain_style)
                .let(ChainStyle::fromValue)
            attributes.boxButtonGravity = array.getInt(R.styleable.ChocolateBoxButton_chocolate_BoxButton_Gravity, theme_chocolate_box_button_gravity)
                .let(Gravity::fromValue)

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
            text = array.getString(R.styleable.ChocolateBoxButton_chocolate_BoxButton_Text) ?: ""

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

            attributes.iconDrawable = array.getDrawable(R.styleable.ChocolateBoxButton_chocolate_BoxButton_Icon)
            attributes.iconTint = array.getColorOrNull(R.styleable.ChocolateBoxButton_chocolate_BoxButton_IconTint)
            attributes.iconSize = array.getDimensionOrNull(R.styleable.ChocolateBoxButton_chocolate_BoxButton_IconSize)
            attributes.iconPadding = array.getDimension(R.styleable.ChocolateBoxButton_chocolate_BoxButton_IconPadding, theme_chocolate_box_button_icon_padding)
            attributes.iconMarginWithText = array.getDimension(R.styleable.ChocolateBoxButton_chocolate_BoxButton_IconMarginWithText, theme_chocolate_box_button_icon_margin_with_text)
        }

        binding.textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, attributes.textSize)
        binding.textView.setTextColor(attributes.textColor)

        if (attributes.hasIconDrawable) {
            if (attributes.isSpecificIconSize) {
                binding.iconContainer.updateLayoutParams<LayoutParams> {
                    width = LayoutParams.WRAP_CONTENT
                    height = LayoutParams.WRAP_CONTENT
                }
                binding.icon.updateLayoutParams<FrameLayout.LayoutParams> {
                    width = attributes.iconSize!!.toInt()
                    height = attributes.iconSize!!.toInt()
                }
                binding.loading.updateLayoutParams<FrameLayout.LayoutParams> {
                    width = attributes.iconSize!!.toInt()
                    height = attributes.iconSize!!.toInt()
                }
            }
            binding.icon.setImageDrawable(attributes.iconDrawable)
            binding.icon.setPadding(attributes.iconPadding.toInt())
            attributes.iconTint?.let(binding.icon::setColorFilter)

            binding.iconContainer.isVisible = true
            binding.icon.isVisible = true
        } else {
            binding.iconContainer.isVisible = false
            binding.icon.isVisible = false

        }

        when (attributes.iconPosition) {
            IconPosition.Left -> {
                when (attributes.chainStyle) {
                    ChainStyle.Packed -> {
                        binding.iconContainer.updateLayoutParams<LayoutParams> {
                            horizontalChainStyle = LayoutParams.CHAIN_PACKED
                            horizontalBias = when (attributes.boxButtonGravity) {
                                Gravity.Start -> 0f
                                Gravity.Center -> 0.5f
                                Gravity.End -> 1f
                            }
                            topToTop = PARENT_ID
                            bottomToBottom = PARENT_ID
                            startToStart = PARENT_ID
                            endToStart = binding.textView.id
                        }
                        binding.textView.updateLayoutParams<LayoutParams> {
                            topToTop = PARENT_ID
                            bottomToBottom = PARENT_ID
                            startToEnd = binding.iconContainer.id
                            endToEnd = PARENT_ID
                            marginStart = attributes.iconMarginWithText.toInt()
                        }
                    }
                    ChainStyle.Spread -> {
                        binding.iconContainer.updateLayoutParams<LayoutParams> {
                            horizontalChainStyle = LayoutParams.CHAIN_SPREAD_INSIDE
                            topToTop = PARENT_ID
                            bottomToBottom = PARENT_ID
                            startToStart = PARENT_ID
                            endToStart = binding.textView.id
                        }
                        binding.textView.updateLayoutParams<LayoutParams> {
                            topToTop = PARENT_ID
                            bottomToBottom = PARENT_ID
                            startToEnd = binding.iconContainer.id
                            endToEnd = PARENT_ID
                        }
                    }
                    ChainStyle.SpreadInside -> {
                        binding.iconContainer.updateLayoutParams<LayoutParams> {
                            matchConstraintPercentHeight = 1f
                            topToTop = PARENT_ID
                            bottomToBottom = PARENT_ID
                            startToStart = PARENT_ID
                            //startToEnd = NO_ID
                        }
                        binding.textView.updateLayoutParams<LayoutParams> {
                            topToTop = PARENT_ID
                            bottomToBottom = PARENT_ID
                            startToEnd = binding.iconContainer.id
                            endToEnd = PARENT_ID
                        }
                    }
                }
            }
            IconPosition.Right -> {
                when (attributes.chainStyle) {
                    ChainStyle.Packed -> {
                        binding.textView.updateLayoutParams<LayoutParams> {
                            horizontalChainStyle = LayoutParams.CHAIN_PACKED
                            horizontalBias = when (attributes.boxButtonGravity) {
                                Gravity.Start -> 0f
                                Gravity.Center -> 0.5f
                                Gravity.End -> 1f
                            }
                            topToTop = PARENT_ID
                            bottomToBottom = PARENT_ID
                            startToStart = PARENT_ID
                            endToStart = binding.iconContainer.id
                            marginEnd = attributes.iconMarginWithText.toInt()
                        }
                        binding.iconContainer.updateLayoutParams<LayoutParams> {
                            horizontalChainStyle = LayoutParams.CHAIN_PACKED
                            topToTop = PARENT_ID
                            bottomToBottom = PARENT_ID
                            startToEnd = binding.textView.id
                            endToEnd = PARENT_ID
                        }
                    }
                    ChainStyle.Spread -> {
                        binding.textView.updateLayoutParams<LayoutParams> {
                            horizontalChainStyle = LayoutParams.CHAIN_SPREAD_INSIDE
                            topToTop = PARENT_ID
                            bottomToBottom = PARENT_ID
                            startToStart = PARENT_ID
                            endToStart = binding.iconContainer.id
                        }
                        binding.iconContainer.updateLayoutParams<LayoutParams> {
                            horizontalChainStyle = LayoutParams.CHAIN_SPREAD_INSIDE
                            topToTop = PARENT_ID
                            bottomToBottom = PARENT_ID
                            startToEnd = binding.textView.id
                            endToEnd = PARENT_ID
                        }
                    }
                    ChainStyle.SpreadInside -> {
                        binding.textView.updateLayoutParams<LayoutParams> {
                            horizontalBias = 0.5f
                            topToTop = PARENT_ID
                            bottomToBottom = PARENT_ID
                            startToStart = PARENT_ID
                            endToStart = binding.iconContainer.id
                        }
                        binding.iconContainer.updateLayoutParams<LayoutParams> {
                            matchConstraintPercentHeight = 1f
                            topToTop = PARENT_ID
                            bottomToBottom = PARENT_ID
                            //startToEnd = NO_ID
                            endToEnd = PARENT_ID
                        }
                    }
                }
            }
        }

        updateIconContainer()

        // 로딩바 색상 설정
        binding.loading.setIndicatorColor(
            attributes.backgroundColors.enabledColor
        )

        binding.iconContainer.addView(binding.icon)
        binding.iconContainer.addView(binding.loading)
        addView(binding.textView)
        addView(binding.iconContainer)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        post {
            when (attributes.iconPosition) {
                IconPosition.Left -> {
                    if (attributes.chainStyle == ChainStyle.SpreadInside) {
                        val iconContainerWidth = binding.iconContainer.width
                        if (iconContainerWidth != binding.textView.marginEnd) {
                            binding.textView.updateLayoutParams<MarginLayoutParams> {
                                marginEnd = binding.iconContainer.width
                            }
                        }
                    }
                }
                IconPosition.Right -> {
                    if (attributes.chainStyle == ChainStyle.SpreadInside) {
                        val iconContainerWidth = binding.iconContainer.width
                        if (iconContainerWidth != binding.textView.marginStart) {
                            binding.textView.updateLayoutParams<MarginLayoutParams> {
                                marginStart = binding.iconContainer.width
                            }
                        }
                    }
                }
            }
            when {
                attributes.iconDrawable == null -> {
                    val indicatorSize = (binding.textView.height * (2/3f)).toInt()
                    binding.loading.indicatorSize = indicatorSize
                    binding.loading.trackThickness = calculateLoadingThickness(indicatorSize.toFloat())
                }
                attributes.isSpecificIconSize.not() -> {
                    val indicatorSize = binding.textView.height
                    binding.loading.indicatorSize = indicatorSize
                    binding.loading.trackThickness = calculateLoadingThickness(indicatorSize.toFloat())
                }
                else -> {
                    val indicatorSize = attributes.iconSize!!.toInt()
                    binding.loading.indicatorSize = indicatorSize
                    binding.loading.trackThickness = calculateLoadingThickness(indicatorSize.toFloat())
                }
            }
        }
    }

    private fun calculateLoadingThickness(diameterDp: Float): Int {
        val baseThickness = 2.dp   // 최소 두께
        val scalingFactor = 0.12f  // 조정 값
        return (baseThickness + scalingFactor * sqrt(diameterDp)).toInt()
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

    private fun updateIconContainer() {
        val isTextVisible = binding.textView.isVisible
        val isSpecificIconSize = attributes.isSpecificIconSize

        binding.iconContainer.updateLayoutParams<LayoutParams> {
            width =  if (isSpecificIconSize) WRAP_CONTENT else MATCH_CONSTRAINT
            height = if (isSpecificIconSize) WRAP_CONTENT else MATCH_CONSTRAINT
        }

        binding.icon.updateLayoutParams<FrameLayout.LayoutParams> {
            width = when {
                isSpecificIconSize -> attributes.iconSize!!.toInt()
                isTextVisible ->      MATCH_PARENT
                else ->               WRAP_CONTENT
            }
            height = width
        }
    }

    /**
     * 버튼의 [enabled] 상태 변경 시 배경 색상, 텍스트 색상, 아이콘 색상을 변경하는 애니메이션을 실행.
     */
    private fun animateButtonEnableStateColorChanged(enabled: Boolean) {
        Log.e(TAG, "animateButtonEnableStateColorChanged: ")
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

        if (binding.icon.isVisible && binding.icon.drawable != null) {
            // 시작 아이콘 색상
            val startIconColor =
                if (enabled) attributes.textDisabledColor
                else attributes.iconTint ?: Color.TRANSPARENT
            // 목표 아이콘 색상
            val targetIconColor =
                if (enabled) attributes.iconTint ?: Color.TRANSPARENT
                else attributes.textDisabledColor
            // 아이콘 색상 변경 애니메이션
            val iconColorAnimator = ValueAnimator.ofArgb(startIconColor, targetIconColor).also {
                it.duration = if (isLayoutTransitionEnabled) ANIMATION_DURATION else 0
                it.interpolator = DecelerateInterpolator(ANIMATION_FACTOR)
                it.addUpdateListener { animation ->
                    val animatedValue = animation.animatedValue as Int
                    binding.icon.setColorFilter(animatedValue)
                }
                it.doOnEnd {
                    binding.icon.setColorFilter(targetIconColor)
                }
            }
            animators.add(iconColorAnimator)
        }

        // 애니메이션 실행
        if (enabledAnimatorSet?.isRunning == true) {
            // 아직 이전의 애니메이션이 실행 중일 때, 이전의 애니메이션을 중지한 뒤 새로운 애니메이션을 실행
            enabledAnimatorSet?.doOnEnd {
                enabledAnimatorSet = AnimatorSet().apply {
                    playTogether(animators.toList())
                    start()
                }
            }
            enabledAnimatorSet?.end()
        } else {
            enabledAnimatorSet = AnimatorSet().apply {
                playTogether(animators.toList())
                start()
            }
        }
    }

    private fun animateLoading(isLoading: Boolean) {
        Log.e(TAG, "animateLoading: ")
        if (binding.icon.drawable == null || binding.icon.isVisible.not()) {
            return
        }

        val animators = mutableListOf<ValueAnimator>()

        val startScale = if (isLoading) 1f else 0.5f
        val targetScale = if (isLoading) 0.5f else 1f
        val iconScaleAnimator = ValueAnimator.ofFloat(startScale, targetScale).also {
            it.duration = if (isLayoutTransitionEnabled) ANIMATION_DURATION else 0
            it.interpolator = DecelerateInterpolator(ANIMATION_FACTOR)
            it.addUpdateListener { animation ->
                val animatedValue = animation.animatedValue as Float
                binding.icon.scaleX = animatedValue
                binding.icon.scaleY = animatedValue
            }
            it.doOnEnd {
                binding.icon.scaleX = targetScale
                binding.icon.scaleY = targetScale
            }
        }
        animators.add(iconScaleAnimator)

        // 애니메이션 실행
        if (loadingAnimatorSet?.isRunning == true) {
            // 아직 이전의 애니메이션이 실행 중일 때, 이전의 애니메이션을 중지한 뒤 새로운 애니메이션을 실행
            loadingAnimatorSet?.doOnEnd {
                loadingAnimatorSet = AnimatorSet().apply {
                    playTogether(animators.toList())
                    start()
                }
            }
            loadingAnimatorSet?.end()
        } else {
            loadingAnimatorSet = AnimatorSet().apply {
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

    internal enum class IconPosition(val value: Int) {
        Left(0),
        Right(1);
        companion object {
            val Default = Left
            fun fromValue(value: Int) = entries.first { it.value == value }
        }
    }

    internal enum class ChainStyle(val value: Int) {
        Packed(0),
        Spread(1),
        SpreadInside(2);
        companion object {
            val Default = Packed
            fun fromValue(value: Int) = entries.first { it.value == value }
        }
    }

    internal enum class Gravity(val value: Int) {
        Start(0),
        Center(1),
        End(2);
        companion object {
            val Default = Center
            fun fromValue(value: Int) = entries.first { it.value == value }
        }
    }

    private data class ViewBinding(
        val loading: CircularProgressIndicator,
        val icon: AppCompatImageView,
        val iconContainer: FrameLayout,
        val textView: AppCompatTextView,
    )

    private data class ChocolateBoxButtonAttributes(
        var isPressEffectEnabled: Boolean = true,
        var pressEffectScaleRatio: Float = 1f,

        var iconPosition: IconPosition = IconPosition.Default,
        var chainStyle: ChainStyle = ChainStyle.Default,
        var boxButtonGravity: Gravity = Gravity.Default,

        var rippleColors: ChocolateColorState = ChocolateColorState.Transparent,
        var ripplePosition: DrawablePosition = DrawablePosition.Foreground,
        var strokeWidth: Float = 0f,
        var strokeColors: ChocolateColorState? = null,
        var backgroundColors: ChocolateColorState = ChocolateColorState.Transparent,
        var backgroundCornerRadius: Float = 0f,

        var textSize: Float = 0f,
        var textColor: Int = Color.TRANSPARENT,
        var textDisabledColor: Int = Color.TRANSPARENT,

        var iconSize: Float? = null,
        var iconDrawable: Drawable? = null,
        var iconTint: Int? = null,
        var iconPadding: Float = 0f,
        var iconMarginWithText: Float = 0f,
    ) {
        val hasIconDrawable: Boolean
            get() = iconDrawable != null
        val isSpecificIconSize: Boolean
            get() = iconSize != null
    }

    companion object {
        private const val ANIMATION_DURATION = 180L
        private const val ANIMATION_FACTOR = 1f
    }
}