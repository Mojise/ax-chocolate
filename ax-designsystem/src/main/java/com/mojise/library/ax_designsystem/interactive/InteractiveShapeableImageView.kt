package com.mojise.library.ax_designsystem.interactive

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import com.google.android.material.imageview.ShapeableImageView

class InteractiveShapeableImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ShapeableImageView(context, attrs, defStyleAttr) {

    private val helper: InteractiveViewHelper = InteractiveViewHelper(this, context, attrs, defStyleAttr)

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        if (isEnabled.not()) {
            return super.dispatchTouchEvent(event)
        }
        helper.onTouchEvent(this, event)
        return super.dispatchTouchEvent(event)
    }
}