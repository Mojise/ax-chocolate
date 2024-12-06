package com.mojise.library.chocolate.button.box

import android.animation.AnimatorSet
import android.animation.LayoutTransition
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
import com.mojise.library.chocolate._internal.chocolate_button_background_color
import com.mojise.library.chocolate._internal.chocolate_button_background_disabled_color
import com.mojise.library.chocolate._internal.chocolate_button_text_color
import com.mojise.library.chocolate._internal.chocolate_button_text_disabled_color
import com.mojise.library.chocolate._internal.exts.getColorOrNull
import com.mojise.library.chocolate.ext.dp
import com.mojise.library.chocolate.ext.sp
import com.mojise.library.chocolate.view.ChocolateView
import com.mojise.library.chocolate.view.util.ChocolateViewAttributesUtil
import com.mojise.library.chocolate.view.helper.ChocolateViewHelper
import com.mojise.library.chocolate._internal.exts.setPaddingBottom
import com.mojise.library.chocolate._internal.exts.setPaddingTop
import com.mojise.library.chocolate.view.model.Attributes
import com.mojise.library.chocolate.view.model.ChocolateColorState
import com.mojise.library.chocolate.view.model.ChocolateTextStyle
import com.mojise.library.chocolate.view.model.PressEffectStrength

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
        get() = attributes.chocolate.isPressEffectEnabled
        set(value) { attributes.chocolate.isPressEffectEnabled = value }

    private val isLayoutTransitionEnabled: Boolean
        get() = layoutTransition != null

    protected val binding: ViewBinding
    protected val attributes: Attributes = ChocolateViewHelper
        .initAttributes(this, context, attrs, defStyleAttr)
    protected val boxButtonAttributes: ChocolateBoxButtonAttributes =
        ChocolateBoxButtonAttributes()

    private var animatorSet: AnimatorSet? = null

    init {
        //Log.e(TAG, "ChocolateBoxButton init")
        val view = inflate(context, R.layout.chocolate_box_button, this)
        view.isClickable = true

        binding = ViewBinding(
            textView = view.findViewById(R.id.chocolate_box_button_text),
            iconLeft = view.findViewById(R.id.chocolate_box_button_icon_left),
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

                boxButtonAttributes.textSize = array.getDimension(R.styleable.ChocolateBoxButton_chocolate_BoxButton_TextSize, TEXT_SIZE_DEFAULT)
                boxButtonAttributes.textColor = array.getColor(R.styleable.ChocolateBoxButton_chocolate_BoxButton_TextColor, chocolate_button_text_color)

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
                boxButtonAttributes.leftIconTint = array.getColorOrNull(R.styleable.ChocolateBoxButton_chocolate_BoxButton_LeftIconTint)
                boxButtonAttributes.leftIconPadding = array.getDimension(R.styleable.ChocolateBoxButton_chocolate_BoxButton_LeftIconPadding, 0f)
                boxButtonAttributes.leftIconMarginWithText = array.getDimension(R.styleable.ChocolateBoxButton_chocolate_BoxButton_LeftIconMarginWithText, ICON_TEXT_MARGIN_DEFAULT)

                boxButtonAttributes.rightIconDrawable = array.getDrawable(R.styleable.ChocolateBoxButton_chocolate_BoxButton_RightIconDrawable)
                boxButtonAttributes.rightIconTint = array.getColorOrNull(R.styleable.ChocolateBoxButton_chocolate_BoxButton_RightIconTint)
                boxButtonAttributes.rightIconPadding = array.getDimension(R.styleable.ChocolateBoxButton_chocolate_BoxButton_RightIconPadding, 0f)
                boxButtonAttributes.rightIconMarginWithText = array.getDimension(R.styleable.ChocolateBoxButton_chocolate_BoxButton_RightIconMarginWithText, ICON_TEXT_MARGIN_DEFAULT)
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
                attributes.chocolate.backgroundColors = if (attributes.android.background == null) {
                    ChocolateColorState.Transparent.apply {
                        enabledColor = array.getColor(R.styleable.chocolateViewAttributes_chocolate_BackgroundColor, chocolate_button_background_color)
                        selectedColor = array.getColorOrNull(R.styleable.chocolateViewAttributes_chocolate_BackgroundSelectedColor)
                            ?: enabledColor
                        disabledColor = array.getColor(R.styleable.chocolateViewAttributes_chocolate_BackgroundDisabledColor, chocolate_button_background_disabled_color)
                    }
                } else {
                    null
                }

                val pressEffectStrengthInt = array.getInt(R.styleable.chocolateViewAttributes_chocolate_PressEffectStrengthLevel, PressEffectStrength.Light.level)
                val pressEffectStrength = PressEffectStrength.valueOf(pressEffectStrengthInt)
                val pressEffectScaleRatio = array.getFloat(R.styleable.chocolateViewAttributes_chocolate_PressEffectScaleRatio, -1f)
                    .takeIf { it in 0f..1f }
                    ?: pressEffectStrength.value

                attributes.chocolate.pressEffectScaleRatio = pressEffectScaleRatio
            }

            // 로딩바 색상 설정
            binding.loading.setIndicatorColor(
                attributes.chocolate.backgroundColors?.enabledColor
                    ?: chocolate_button_background_color
            )
        } catch (e: Exception) {
            Log.w(TAG, "ChocolateBoxButton error", e)
        }

        ChocolateViewHelper.setRippleBackgroundOrForeground(this, attributes)
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean = try {
        if (isEnabled && isClickable && isPressEffectEnabled) {
            ChocolateViewHelper.showPressEffectOnTouch(this, event, attributes.chocolate.pressEffectScaleRatio)
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
        super.setBackground(background)

    }

    /**
     * 버튼의 [enabled] 상태 변경 시 배경 색상, 텍스트 색상, 아이콘 색상을 변경하는 애니메이션을 실행.
     */
    private fun animateButtonEnableStateColorChanged(enabled: Boolean) {
        if (attributes.chocolate.backgroundColors == null) {
            return
        }

        val animators = mutableListOf<ValueAnimator>()

        // 시작 배경 색상
        val startBackgroundColor = if (enabled)
            attributes.chocolate.backgroundColors?.disabledColor ?: chocolate_button_background_disabled_color
            else attributes.chocolate.backgroundColors?.enabledColor ?: Color.TRANSPARENT

        // 목표 배경 색상
        val targetBackgroundColor = if (enabled)
            attributes.chocolate.backgroundColors?.enabledColor ?: Color.TRANSPARENT
            else attributes.chocolate.backgroundColors?.disabledColor ?: chocolate_button_background_disabled_color

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
            if (enabled) chocolate_button_text_disabled_color
            else boxButtonAttributes.textColor
        // 목표 텍스트 색상
        val targetTextColor =
            if (enabled) boxButtonAttributes.textColor
            else chocolate_button_text_disabled_color
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
                if (enabled) chocolate_button_text_disabled_color
                else boxButtonAttributes.leftIconTint ?: Color.TRANSPARENT
            // 목표 Left 아이콘 색상
            val targetIconLeftColor =
                if (enabled) boxButtonAttributes.leftIconTint ?: Color.TRANSPARENT
                else chocolate_button_text_disabled_color
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
                if (enabled) chocolate_button_text_disabled_color
                else boxButtonAttributes.rightIconTint ?: Color.TRANSPARENT
            // 목표 Right 아이콘 색상
            val targetIconRightColor =
                if (enabled) boxButtonAttributes.rightIconTint ?: Color.TRANSPARENT
                else chocolate_button_text_disabled_color
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
        var textSize: Float = TEXT_SIZE_DEFAULT,
        var textColor: Int = Color.WHITE,
        var leftIconDrawable: Drawable? = null,
        var leftIconTint: Int? = null,
        var leftIconPadding: Float = 0f,
        var leftIconMarginWithText: Float = ICON_TEXT_MARGIN_DEFAULT,
        var rightIconDrawable: Drawable? = null,
        var rightIconTint: Int? = null,
        var rightIconPadding: Float = 0f,
        var rightIconMarginWithText: Float = ICON_TEXT_MARGIN_DEFAULT,
    )

    companion object {
        private const val ANIMATION_DURATION = 180L
        private const val ANIMATION_FACTOR = 1f
        private val TEXT_SIZE_DEFAULT = 18f.sp
        private val ICON_TEXT_MARGIN_DEFAULT = 4f.dp
    }
}