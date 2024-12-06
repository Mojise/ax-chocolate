package com.mojise.library.chocolate._internal.exts

import android.content.res.TypedArray
import androidx.annotation.ColorInt
import androidx.annotation.StyleableRes

@ColorInt
internal fun TypedArray.getColorOrNull(@StyleableRes index: Int): Int? {
    return if (hasValue(index)) getColor(index, 0) else null
}

internal fun TypedArray.getDimensionOrNull(@StyleableRes index: Int): Float? {
    return if (hasValue(index)) getDimension(index, 0f) else null
}