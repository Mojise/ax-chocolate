package com.mojise.library.chocolate.view.helper

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import com.mojise.library.chocolate.util.TAG
import com.mojise.library.chocolate.view.model.ChocolatePadding

object ChocolateViewAttributesUtil {

    /**
     * Android 속성을 가져와서 Padding 값을 반환합니다.
     */
    fun obtainAndroidPaddings(
        context: Context,
        attrs: AttributeSet?,
        defaultVerticalPadding: Int,
        defaultHorizontalPadding: Int,
    ): ChocolatePadding {
        var paddingTop = defaultVerticalPadding
        var paddingBottom = defaultVerticalPadding
        var paddingStart = defaultHorizontalPadding
        var paddingEnd = defaultHorizontalPadding

        try {
            context.obtainStyledAttributes(attrs, intArrayOf(
                android.R.attr.padding,
                android.R.attr.paddingVertical,
                android.R.attr.paddingHorizontal,
                android.R.attr.paddingTop,
                android.R.attr.paddingBottom,
                android.R.attr.paddingLeft,
                android.R.attr.paddingRight,
                android.R.attr.paddingStart,
                android.R.attr.paddingEnd,
            )).use { array ->
                array.getDimensionPixelSize(0, -1).takeIf { it >= 0 }?.let { padding ->
                    paddingTop = padding
                    paddingBottom = padding
                    paddingStart = padding
                    paddingEnd = padding
                }
                array.getDimensionPixelSize(1, -1).takeIf { it >= 0 }?.let { padding ->
                    paddingTop = padding
                    paddingBottom = padding
                }
                array.getDimensionPixelSize(2, -1).takeIf { it >= 0 }?.let { padding ->
                    paddingStart = padding
                    paddingEnd = padding
                }
                array.getDimensionPixelSize(3, -1).takeIf { it >= 0 }?.let { padding ->
                    paddingTop = padding
                }
                array.getDimensionPixelSize(4, -1).takeIf { it >= 0 }?.let { padding ->
                    paddingBottom = padding
                }
                array.getDimensionPixelSize(5, -1).takeIf { it >= 0 }?.let { padding ->
                    paddingStart = padding
                }
                array.getDimensionPixelSize(6, -1).takeIf { it >= 0 }?.let { padding ->
                    paddingEnd = padding
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, "obtainAndroidPaddings error", e)
        }

        return ChocolatePadding(
            top = paddingTop,
            bottom = paddingBottom,
            start = paddingStart,
            end = paddingEnd,
        )
    }
}