package com.mojise.library.chocolate.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.FrameLayout
import com.mojise.library.chocolate._internal.TAG
import com.mojise.library.chocolate.view.helper.ChocolateViewHelper
import com.mojise.library.chocolate.view.model.Attributes

open class ChocolateFrameLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes), ChocolateView {

    /**
     * 터치 시 눌림 효과 사용 여부
     */
    override var isPressEffectEnabled: Boolean
        get() = attributes.chocolate.isPressEffectEnabled
        set(value) { attributes.chocolate.isPressEffectEnabled = value }

    protected val attributes: Attributes = ChocolateViewHelper
        .initAttributes(this, context, attrs, defStyleAttr, defStyleRes)

    init {
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
}