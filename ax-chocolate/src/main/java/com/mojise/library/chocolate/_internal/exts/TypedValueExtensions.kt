package com.mojise.library.chocolate._internal.exts

import android.content.Context
import android.util.Log
import android.util.TypedValue
import androidx.annotation.AnyRes
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import com.mojise.library.chocolate._internal.TAG

@ColorInt
internal fun Context.getColorFromTheme(@AttrRes resId: Int): Int {
    val typedValue = TypedValue()
    val resolved = theme.resolveAttribute(resId, typedValue, true)
    if (resolved.not()) {
        Log.w(TAG, "Attribute not found in theme (resId=$resId)")
    }
    return typedValue.data
}

internal fun Context.getDimensionFromTheme(@AttrRes resId: Int): Float {
    val typedValue = TypedValue()
    val resolved = theme.resolveAttribute(resId, typedValue, true)
    if (resolved.not()) {
        Log.w(TAG, "Attribute not found in theme (resId=$resId)")
    }
    return typedValue.getDimension(resources.displayMetrics)
}

@AnyRes
internal fun Context.getResourceIdFromTheme(@AttrRes resId: Int): Int {
    val typedValue = TypedValue()
    val resolved = theme.resolveAttribute(resId, typedValue, true)
    val resourceId = typedValue.resourceId
    if (resolved.not()) {
        throw IllegalArgumentException("Attribute not found in theme (resId=$resId)")
    }
    if (resourceId == 0) {
        throw IllegalArgumentException("Resource not found in theme (resId=$resId)")
    }
    return typedValue.resourceId
}

@AnyRes
internal fun Context.getResourceIdOrNullFromTheme(@AttrRes resId: Int): Int? {
    val typedValue = TypedValue()
    val resolved = theme.resolveAttribute(resId, typedValue, true)
    val resourceId = typedValue.resourceId
    if (resolved.not()) {
        Log.w(TAG, "Attribute not found in theme (resId=$resId)")
    }
    if (resourceId == 0) {
        Log.w(TAG, "Resource not found in theme (resId=$resId)")
    }
    return if (resolved && resourceId != 0) typedValue.resourceId else null
}

internal fun Context.getBoolean(@AttrRes resId: Int, defaultValue: Boolean): Boolean {
    val typedValue = TypedValue()
    val resolved = theme.resolveAttribute(resId, typedValue, true)
    return if (resolved) typedValue.data != 0 else defaultValue
}

internal fun Context.getInt(@AttrRes resId: Int, defaultValue: Int): Int {
    val typedValue = TypedValue()
    val resolved = theme.resolveAttribute(resId, typedValue, true)
    return if (resolved) typedValue.data else defaultValue
}

internal fun Context.getIntOrNull(@AttrRes resId: Int): Int? {
    val typedValue = TypedValue()
    val resolved = theme.resolveAttribute(resId, typedValue, true)
    return if (resolved) typedValue.data else null
}

internal fun Context.getFloat(@AttrRes resId: Int, defaultValue: Float): Float {
    val typedValue = TypedValue()
    val resolved = theme.resolveAttribute(resId, typedValue, true)
    return if (resolved) typedValue.float else defaultValue
}

internal fun Context.getFloatOrNull(@AttrRes resId: Int): Float? {
    val typedValue = TypedValue()
    val resolved = theme.resolveAttribute(resId, typedValue, true)
    return if (resolved) typedValue.float else null
}