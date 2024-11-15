package com.mojise.library.chocolate.view

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatTextView
import com.mojise.library.chocolate.R
import com.mojise.library.chocolate.util.TAG
import com.mojise.library.chocolate.view.helper.ChocolateViewHelper
import com.mojise.library.chocolate.view.model.ChocolateColorState

open class ChocolateTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr), ChocolateView {

    /**
     * 터치 시 눌림 효과 사용 여부
     */
    override var isPressEffectEnabled: Boolean
        get() = helper.attributes.isPressEffectEnabled
        set(value) { helper.attributes.isPressEffectEnabled = value }

    private val attributes = ChocolateTextViewAttributes()

    private val helper: ChocolateViewHelper = ChocolateViewHelper(this, context, attrs, defStyleAttr)

    init {
        Log.d(TAG, "ChocolateTextView init")

        val androidTypedArray = context.obtainStyledAttributes(attrs, intArrayOf(android.R.attr.textColor), defStyleAttr, 0)
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ChocolateTextView, defStyleAttr, 0)

        try {
            var textColorStateList: ColorStateList? = androidTypedArray
                .getColorStateList(0)

            when {
                // android:text 색상이 지정되어 있지 않은 경우, ChocolateTextView 속성을 사용
                textColorStateList == null -> {
                    textColorStateList = ChocolateColorState(
                        enabledColor = context.getColor(R.color.chocolate_text_color_black),
                        selectedColor = typedArray.getColor(R.styleable.ChocolateTextView_chocolateTextSelectedColor, NO_ID)
                            .takeIf { it != NO_ID }
                            ?: context.getColor(R.color.chocolate_text_color_black),
                        disabledColor = typedArray.getColor(R.styleable.ChocolateTextView_chocolateTextDisabledColor, context.getColor(R.color.chocolate_button_text_disabled))
                    ).toColorStateList()
                }
                // android:text 기본 색상만 있고 상태는 없는 경우, ChocolateTextView 속성을 사용
                textColorStateList.isStateful.not() -> {
                    textColorStateList = ChocolateColorState(
                        enabledColor = textColorStateList.defaultColor,
                        selectedColor = typedArray.getColor(R.styleable.ChocolateTextView_chocolateTextSelectedColor, NO_ID)
                            .takeIf { it != NO_ID }
                            ?: textColorStateList.defaultColor,
                        disabledColor = typedArray.getColor(R.styleable.ChocolateTextView_chocolateTextDisabledColor, context.getColor(R.color.chocolate_button_text_disabled))
                    ).toColorStateList()
                }
            }

            setTextColor(textColorStateList)
        } catch (e: Exception) {
            Log.w(TAG, "ChocolateTextView init error", e)
        } finally {
            androidTypedArray.recycle()
            typedArray.recycle()
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean = try {
        if (isEnabled && isPressEffectEnabled) {
            helper.showPressEffectOnTouch(this, event)
        }
        super.dispatchTouchEvent(event)
    } catch (e: Exception) {
        // 터치 이벤트가 빠르게 연속적으로 발생하는 것을 방지하거나, 이미 파괴된 뷰에 이벤트가 전달되었을 경우 등을 위한 예외 처리
        Log.w(TAG, "dispatchTouchEvent error", e)
        false
    }

    private data class ChocolateTextViewAttributes(
        var textColor: ColorStateList? = null
    )
}