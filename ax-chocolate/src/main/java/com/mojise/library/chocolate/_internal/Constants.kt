package com.mojise.library.chocolate._internal

import android.content.Context
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.content.res.ResourcesCompat
import com.mojise.library.chocolate.R
import com.mojise.library.chocolate.app.AxChocolateDesignSystem

internal const val TAG = "ax-chocolate"

@get:ColorInt
internal val View.chocolate_primary_color: Int
    get() = ResourcesCompat.getColor(resources, R.color.chocolate_primary_color, context.theme)

@get:ColorInt
internal val View.chocolate_ripple_color_black: Int
    get() = ResourcesCompat.getColor(resources, R.color.chocolate_ripple_color_black, context.theme)

@get:ColorInt
internal val View.chocolate_button_text_color: Int
    get() = ResourcesCompat.getColor(resources, AxChocolateDesignSystem.BoxButton.textColorResId, context.theme)

@get:ColorInt
internal val View.chocolate_button_text_disabled_color: Int
    get() = ResourcesCompat.getColor(resources, AxChocolateDesignSystem.BoxButton.textDisabledColorResId, context.theme)

@get:ColorInt
internal val View.chocolate_button_background_color: Int
    get() = ResourcesCompat.getColor(resources, AxChocolateDesignSystem.BoxButton.backgroundColorResId, context.theme)

@get:ColorInt
internal val View.chocolate_button_background_disabled_color: Int
    get() = ResourcesCompat.getColor(resources, AxChocolateDesignSystem.BoxButton.backgroundDisabledColorResId, context.theme)

@get:ColorInt
internal val Context.chocolate_bottom_sheet_grip_bar_color: Int
    get() = ResourcesCompat.getColor(resources, R.color.chocolate_bottom_sheet_grip_bar, theme)

@get:ColorInt
internal val Context.chocolate_bottom_sheet_background_color: Int
    get() = ResourcesCompat.getColor(resources, R.color.chocolate_bottom_sheet_background, theme)

@get:ColorInt
internal val Context.chocolate_bottom_sheet_outside_color: Int
    get() = ResourcesCompat.getColor(resources, R.color.chocolate_bottom_sheet_outside_color, theme)