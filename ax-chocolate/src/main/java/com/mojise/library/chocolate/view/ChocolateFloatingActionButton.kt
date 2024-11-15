package com.mojise.library.chocolate.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mojise.library.chocolate.util.TAG
import com.mojise.library.chocolate.view.helper.ChocolateViewHelper

open class ChocolateFloatingActionButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FloatingActionButton(context, attrs, defStyleAttr), ChocolateView {

    /**
     * 터치 시 눌림 효과 사용 여부
     */
    override var isPressEffectEnabled: Boolean
        get() = helper.attributes.isPressEffectEnabled
        set(value) { helper.attributes.isPressEffectEnabled = value }

    private val helper: ChocolateViewHelper = ChocolateViewHelper(this, context, attrs, defStyleAttr)

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
}