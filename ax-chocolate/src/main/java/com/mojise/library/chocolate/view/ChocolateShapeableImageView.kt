package com.mojise.library.chocolate.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import androidx.core.view.setPadding
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.CornerFamily
import com.mojise.library.chocolate.R
import com.mojise.library.chocolate.ext.dp
import com.mojise.library.chocolate.util.TAG
import com.mojise.library.chocolate.view.helper.ChocolateViewHelper
import kotlin.math.min

open class ChocolateShapeableImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : ShapeableImageView(context, attrs, defStyleAttr), ChocolateView {

    /**
     * 터치 시 눌림 효과 사용 여부
     */
    override var isPressEffectEnabled: Boolean
        get() = helper.attributes.isPressEffectEnabled
        set(value) { helper.attributes.isPressEffectEnabled = value }

    private val helper: ChocolateViewHelper = ChocolateViewHelper(this, context, attrs, defStyleAttr)

    private val attributes: Attributes = Attributes(
        cornerShape = CornerShape.None,
        cornerRadius = CornerRadius(0f, 0f, 0f, 0f),
    )

    init {
        Log.d(TAG, "ChocolateShapeableImageView init")

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ChocolateShapeableImageView, defStyleAttr, 0)

        try {
            typedArray.getDimension(R.styleable.ChocolateShapeableImageView_chocolateImageCornerRadius, NONE)
                .takeIf { it != NONE }
                ?.let { radius ->
                    attributes.cornerRadius.topLeft = radius
                    attributes.cornerRadius.topRight = radius
                    attributes.cornerRadius.bottomRight = radius
                    attributes.cornerRadius.bottomLeft = radius
                }
            typedArray.getString(R.styleable.ChocolateShapeableImageView_chocolateImageCornerRadiusDpList)
                ?.split(",")
                ?.mapNotNull(String::toFloatOrNull)
                ?.takeIf { it.size == 4 }
                ?.let { (topLeft, topRight, bottomLeft, bottomRight) ->
                    attributes.cornerRadius.topLeft = topLeft.dp
                    attributes.cornerRadius.topRight = topRight.dp
                    attributes.cornerRadius.bottomLeft = bottomLeft.dp
                    attributes.cornerRadius.bottomRight = bottomRight.dp
                }
            typedArray.getDimension(R.styleable.ChocolateShapeableImageView_chocolateImageCornerRadiusTop, NONE)
                .takeIf { it != NONE }
                ?.let { top ->
                    attributes.cornerRadius.topLeft = top
                    attributes.cornerRadius.topRight = top
                }
            typedArray.getDimension(R.styleable.ChocolateShapeableImageView_chocolateImageCornerRadiusBottom, NONE)
                .takeIf { it != NONE }
                ?.let { bottom ->
                    attributes.cornerRadius.bottomRight = bottom
                    attributes.cornerRadius.bottomLeft = bottom
                }
            typedArray.getDimension(R.styleable.ChocolateShapeableImageView_chocolateImageCornerRadiusTopLeft, NONE)
                .takeIf { it != NONE }
                ?.let { topLeft ->
                    attributes.cornerRadius.topLeft = topLeft
                }
            typedArray.getDimension(R.styleable.ChocolateShapeableImageView_chocolateImageCornerRadiusTopRight, NONE)
                .takeIf { it != NONE }
                ?.let { topRight ->
                    attributes.cornerRadius.topRight = topRight
                }
            typedArray.getDimension(R.styleable.ChocolateShapeableImageView_chocolateImageCornerRadiusBottomLeft, NONE)
                .takeIf { it != NONE }
                ?.let { bottomLeft ->
                    attributes.cornerRadius.bottomLeft = bottomLeft
                }
            typedArray.getDimension(R.styleable.ChocolateShapeableImageView_chocolateImageCornerRadiusBottomRight, NONE)
                .takeIf { it != NONE }
                ?.let { bottomRight ->
                    attributes.cornerRadius.bottomRight = bottomRight
                }

            // CornerShape의 Default 값은
            // CornerRadius가 모두 0이면 CornerShape.None, 아니면 CornerShape.Rounded
            val defaultCornerShape = if (attributes.cornerRadius.noSize())
                CornerShape.None
                else CornerShape.Rounded

            typedArray.getInt(R.styleable.ChocolateShapeableImageView_chocolateImageCornerShape, defaultCornerShape.value).let { shape ->
                attributes.cornerShape = CornerShape.entries.getOrNull(shape) ?: defaultCornerShape
            }
        } catch (e: Exception) {
            Log.w(TAG, "ChocolateShapeableImageView error", e)
        } finally {
            typedArray.recycle()
        }

        //Log.d(TAG, "ChocolateShapeableImageView attributes=$attributes")

        when (attributes.cornerShape) {
            CornerShape.Rounded -> {
                shapeAppearanceModel = shapeAppearanceModel.toBuilder()
                    .setTopLeftCorner(CornerFamily.ROUNDED, attributes.cornerRadius.topLeft)
                    .setTopRightCorner(CornerFamily.ROUNDED, attributes.cornerRadius.topRight)
                    .setBottomLeftCorner(CornerFamily.ROUNDED, attributes.cornerRadius.bottomLeft)
                    .setBottomRightCorner(CornerFamily.ROUNDED, attributes.cornerRadius.bottomRight)
                    .build()
            }
            CornerShape.Circle -> {
                // Nothing to do. Circle shape is set in onSizeChanged
            }
            CornerShape.Cut -> {
                shapeAppearanceModel = shapeAppearanceModel.toBuilder()
                    .setTopLeftCorner(CornerFamily.CUT, attributes.cornerRadius.topLeft)
                    .setTopRightCorner(CornerFamily.CUT, attributes.cornerRadius.topRight)
                    .setBottomLeftCorner(CornerFamily.CUT, attributes.cornerRadius.bottomLeft)
                    .setBottomRightCorner(CornerFamily.CUT, attributes.cornerRadius.bottomRight)
                    .build()
            }
            CornerShape.Diamond -> {
                // Nothing to do. Diamond shape is set in onSizeChanged
            }
            CornerShape.None -> {
                // Nothing to do.
            }
        }
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)

        // Padding이 적용된 width, height
        val widthWithPadding = width - (paddingLeft + paddingRight)
        val heightWithPadding = height - (paddingTop + paddingBottom)

        // Circle, Diamond 모양일 때의 반지름 (width, height 중 작은 값)
        val radius = min(widthWithPadding, heightWithPadding) / 2f

        // Circle, Diamond 모양은 width, height가 변경될 때마다 적용
        when (attributes.cornerShape) {
            CornerShape.Circle -> {
                shapeAppearanceModel = shapeAppearanceModel.toBuilder()
                    .setAllCorners(CornerFamily.ROUNDED, radius)
                    .build()
            }
            CornerShape.Diamond -> {
                shapeAppearanceModel = shapeAppearanceModel.toBuilder()
                    .setAllCorners(CornerFamily.CUT, radius)
                    .build()
            }
            else -> { /* Nothing to do */ }
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

    private data class Attributes(
        var cornerShape: CornerShape,
        val cornerRadius: CornerRadius,
    )

    private data class CornerRadius(
        var topLeft: Float,
        var topRight: Float,
        var bottomRight: Float,
        var bottomLeft: Float,
    ) {
        fun noSize() = topLeft == 0f && topRight == 0f && bottomRight == 0f && bottomLeft == 0f
    }

    private enum class CornerShape(val value: Int) {
        Rounded(0),
        Circle(1),
        Cut(2),
        Diamond(3),
        None(4),
    }

    companion object {
        const val NONE = 0f
    }
}