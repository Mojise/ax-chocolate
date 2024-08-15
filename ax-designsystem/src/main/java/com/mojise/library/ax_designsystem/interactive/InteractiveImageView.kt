package com.mojise.library.ax_designsystem.interactive

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatImageView

class InteractiveImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private val helper: InteractiveViewHelper = InteractiveViewHelper(this, context, attrs, defStyleAttr)

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        if (isEnabled.not()) {
            return super.dispatchTouchEvent(event)
        }
        helper.onTouchEvent(this, event)
        return super.dispatchTouchEvent(event)
    }
}