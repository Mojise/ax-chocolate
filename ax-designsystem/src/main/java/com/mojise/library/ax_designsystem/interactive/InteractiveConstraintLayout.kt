package com.mojise.library.ax_designsystem.interactive

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.constraintlayout.widget.ConstraintLayout

class InteractiveConstraintLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val helper: InteractiveViewHelper = InteractiveViewHelper(this, context, attrs, defStyleAttr, defStyleRes)

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        if (isEnabled.not()) {
            return super.dispatchTouchEvent(event)
        }
        helper.onTouchEvent(this, event)
        return super.dispatchTouchEvent(event)
    }
}