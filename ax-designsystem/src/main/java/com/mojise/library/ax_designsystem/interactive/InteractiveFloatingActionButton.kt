package com.mojise.library.ax_designsystem.interactive

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import com.google.android.material.floatingactionbutton.FloatingActionButton

open class InteractiveFloatingActionButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FloatingActionButton(context, attrs, defStyleAttr) {

    private val helper: InteractiveViewHelper = InteractiveViewHelper(this, context, attrs, defStyleAttr)

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean = try {
        if (isEnabled) {
            helper.onTouchEvent(this, event)
        }
        super.dispatchTouchEvent(event)
    } catch (e: Exception) {
        // 터치 이벤트가 빠르게 연속적으로 발생하는 것을 방지하거나, 이미 파괴된 뷰에 이벤트가 전달되었을 경우 예외 처리
        Log.w("ax-designsystem-interactive", "dispatchTouchEvent error", e)
        false
    }
}