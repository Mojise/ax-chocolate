package com.mojise.library.chocolate.ext

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.drawable.BitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory

/**
 * Convert BitmapDrawable to RoundedBitmapDrawable.
 */
fun BitmapDrawable.toRoundedBitmapDrawable(context: Context, cornerRadius: Float): RoundedBitmapDrawable {
    return RoundedBitmapDrawableFactory.create(context.resources, bitmap).apply {
        this.cornerRadius = cornerRadius
    }
}

/**
 * Convert BitmapDrawable to RoundedBitmapDrawable.
 */
fun BitmapDrawable.toRoundedCornerDrawable(
    context: Context,
    topLeft: Float,
    topRight: Float,
    bottomRight: Float,
    bottomLeft: Float
): BitmapDrawable {
    val width = bitmap.width
    val height = bitmap.height

    // 결과 비트맵 생성
    val outputBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(outputBitmap)

    // Paint와 BitmapShader 설정
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val shader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
    paint.shader = shader

    // 둥근 사각형 Path 생성
    val path = Path()
    val radii = floatArrayOf(
        topLeft, topLeft, // Top-left
        topRight, topRight, // Top-right
        bottomRight, bottomRight, // Bottom-right
        bottomLeft, bottomLeft // Bottom-left
    )
    path.addRoundRect(
        RectF(0f, 0f, width.toFloat(), height.toFloat()),
        radii,
        Path.Direction.CW
    )

    // Path를 캔버스에 그려 비트맵 생성
    canvas.drawPath(path, paint)

    // BitmapDrawable로 반환
    return BitmapDrawable(context.resources, outputBitmap)
}