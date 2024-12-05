package com.mojise.library.chocolate.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.CornerFamily
import com.mojise.library.chocolate.R
import com.mojise.library.chocolate.ext.dp
import com.mojise.library.chocolate.util.TAG
import com.mojise.library.chocolate.view.helper.ChocolateViewHelper
import com.mojise.library.chocolate.view.model.Attributes
import kotlin.math.min

open class ChocolateShapeableImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : ShapeableImageView(context, attrs, defStyleAttr), ChocolateView {

    /**
     * 터치 시 눌림 효과 사용 여부
     */
    override var isPressEffectEnabled: Boolean
        get() = attributes.chocolate.isPressEffectEnabled
        set(value) { attributes.chocolate.isPressEffectEnabled = value }

    protected val attributes: Attributes = ChocolateViewHelper
        .initAttributes(context, attrs, defStyleAttr)

    private val shapeableAttributes: ShapeableAttributes = ShapeableAttributes(
        cornerShape = CornerShape.None,
        cornerRadius = CornerRadius(0f, 0f, 0f, 0f),
    )

    init {
        Log.d(TAG, "ChocolateShapeableImageView init")

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ChocolateShapeableImageView, defStyleAttr, 0)

        try {
            typedArray.getDimension(R.styleable.ChocolateShapeableImageView_chocolate_ShapeableImage_CornerRadius, NONE)
                .takeIf { it != NONE }
                ?.let { radius ->
                    shapeableAttributes.cornerRadius.topLeft = radius
                    shapeableAttributes.cornerRadius.topRight = radius
                    shapeableAttributes.cornerRadius.bottomRight = radius
                    shapeableAttributes.cornerRadius.bottomLeft = radius
                }
            typedArray.getString(R.styleable.ChocolateShapeableImageView_chocolate_ShapeableImage_CornerRadiusDpList)
                ?.split(",")
                ?.mapNotNull(String::toFloatOrNull)
                ?.takeIf { it.size == 4 }
                ?.let { (topLeft, topRight, bottomLeft, bottomRight) ->
                    shapeableAttributes.cornerRadius.topLeft = topLeft.dp
                    shapeableAttributes.cornerRadius.topRight = topRight.dp
                    shapeableAttributes.cornerRadius.bottomLeft = bottomLeft.dp
                    shapeableAttributes.cornerRadius.bottomRight = bottomRight.dp
                }
            typedArray.getDimension(R.styleable.ChocolateShapeableImageView_chocolate_ShapeableImage_CornerRadiusTop, NONE)
                .takeIf { it != NONE }
                ?.let { top ->
                    shapeableAttributes.cornerRadius.topLeft = top
                    shapeableAttributes.cornerRadius.topRight = top
                }
            typedArray.getDimension(R.styleable.ChocolateShapeableImageView_chocolate_ShapeableImage_CornerRadiusBottom, NONE)
                .takeIf { it != NONE }
                ?.let { bottom ->
                    shapeableAttributes.cornerRadius.bottomRight = bottom
                    shapeableAttributes.cornerRadius.bottomLeft = bottom
                }
            typedArray.getDimension(R.styleable.ChocolateShapeableImageView_chocolate_ShapeableImage_CornerRadiusTopLeft, NONE)
                .takeIf { it != NONE }
                ?.let { topLeft ->
                    shapeableAttributes.cornerRadius.topLeft = topLeft
                }
            typedArray.getDimension(R.styleable.ChocolateShapeableImageView_chocolate_ShapeableImage_CornerRadiusTopRight, NONE)
                .takeIf { it != NONE }
                ?.let { topRight ->
                    shapeableAttributes.cornerRadius.topRight = topRight
                }
            typedArray.getDimension(R.styleable.ChocolateShapeableImageView_chocolate_ShapeableImage_CornerRadiusBottomLeft, NONE)
                .takeIf { it != NONE }
                ?.let { bottomLeft ->
                    shapeableAttributes.cornerRadius.bottomLeft = bottomLeft
                }
            typedArray.getDimension(R.styleable.ChocolateShapeableImageView_chocolate_ShapeableImage_CornerRadiusBottomRight, NONE)
                .takeIf { it != NONE }
                ?.let { bottomRight ->
                    shapeableAttributes.cornerRadius.bottomRight = bottomRight
                }

            // CornerShape의 Default 값은
            // CornerRadius가 모두 0이면 CornerShape.None, 아니면 CornerShape.Rounded
            val defaultCornerShape = if (shapeableAttributes.cornerRadius.noSize())
                CornerShape.None
                else CornerShape.Rounded

            typedArray.getInt(R.styleable.ChocolateShapeableImageView_chocolate_ShapeableImage_CornerShape, defaultCornerShape.value).let { shape ->
                shapeableAttributes.cornerShape = CornerShape.entries.getOrNull(shape) ?: defaultCornerShape
            }
        } catch (e: Exception) {
            Log.w(TAG, "ChocolateShapeableImageView error", e)
        } finally {
            typedArray.recycle()
        }

        //Log.d(TAG, "ChocolateShapeableImageView attributes=$attributes")

        when (shapeableAttributes.cornerShape) {
            CornerShape.Rounded -> {
                shapeAppearanceModel = shapeAppearanceModel.toBuilder()
                    .setTopLeftCorner(CornerFamily.ROUNDED, shapeableAttributes.cornerRadius.topLeft)
                    .setTopRightCorner(CornerFamily.ROUNDED, shapeableAttributes.cornerRadius.topRight)
                    .setBottomLeftCorner(CornerFamily.ROUNDED, shapeableAttributes.cornerRadius.bottomLeft)
                    .setBottomRightCorner(CornerFamily.ROUNDED, shapeableAttributes.cornerRadius.bottomRight)
                    .build()
            }
            CornerShape.Circle -> {
                // Nothing to do. Circle shape is set in onSizeChanged
            }
            CornerShape.Cut -> {
                shapeAppearanceModel = shapeAppearanceModel.toBuilder()
                    .setTopLeftCorner(CornerFamily.CUT, shapeableAttributes.cornerRadius.topLeft)
                    .setTopRightCorner(CornerFamily.CUT, shapeableAttributes.cornerRadius.topRight)
                    .setBottomLeftCorner(CornerFamily.CUT, shapeableAttributes.cornerRadius.bottomLeft)
                    .setBottomRightCorner(CornerFamily.CUT, shapeableAttributes.cornerRadius.bottomRight)
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
        when (shapeableAttributes.cornerShape) {
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
            ChocolateViewHelper.showPressEffectOnTouch(this, event, attributes.chocolate.pressEffectScaleRatio)
        }
        super.dispatchTouchEvent(event)
    } catch (e: Exception) {
        // 터치 이벤트가 빠르게 연속적으로 발생하는 것을 방지하거나, 이미 파괴된 뷰에 이벤트가 전달되었을 경우 등을 위한 예외 처리
        Log.w(TAG, "dispatchTouchEvent error", e)
        false
    }

    private data class ShapeableAttributes(
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