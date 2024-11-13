package com.mojise.library.ax_chocolate.interactive

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.FrameLayout
import com.mojise.library.ax_chocolate.util.TAG

open class ChocolateInteractiveFrameLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val helper: ChocolateInteractiveViewHelper = ChocolateInteractiveViewHelper(this, context, attrs, defStyleAttr, defStyleRes)

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean = try {
        if (isEnabled) {
            helper.onTouchEvent(this, event)
        }
        super.dispatchTouchEvent(event)
    } catch (e: Exception) {
        // 터치 이벤트가 빠르게 연속적으로 발생하는 것을 방지하거나, 이미 파괴된 뷰에 이벤트가 전달되었을 경우 예외 처리
        Log.w(TAG, "dispatchTouchEvent error", e)
        false
    }
}