package com.mojise.library.chocolate.ext

import android.content.res.TypedArray
import android.os.Build
import androidx.annotation.ColorInt
import androidx.annotation.StyleableRes

inline fun <R> TypedArray.useCompat(block: (TypedArray) -> R): R {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        return use(block)
    } else {
        try {
            return block(this)
        } finally {
            try {
                recycle()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

@ColorInt
fun TypedArray.getColorOrNull(@StyleableRes index: Int): Int? {
    return if (hasValue(index)) getColor(index, 0) else null
}

fun TypedArray.getDimensionOrNull(@StyleableRes index: Int): Float? {
    return if (hasValue(index)) getDimension(index, 0f) else null
}