package com.mojise.library.chocolate._internal.theme

import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.FontRes
import com.mojise.library.chocolate.R
import com.mojise.library.chocolate._internal.exts.getColorFromTheme
import com.mojise.library.chocolate._internal.exts.getDimensionFromTheme
import com.mojise.library.chocolate._internal.exts.getInt
import com.mojise.library.chocolate._internal.exts.getResourceIdOrNullFromTheme
import com.mojise.library.chocolate.view.model.ChocolateTextStyle

internal val View.theme_chocolate_box_button_text_size: Float
    get() = context.getDimensionFromTheme(R.attr.chocolate_BoxButton_TextSize)

internal val View.theme_chocolate_box_button_text_padding_top: Float
    get() = context.getDimensionFromTheme(R.attr.chocolate_BoxButton_TextPaddingTop)

internal val View.theme_chocolate_box_button_text_padding_bottom: Float
    get() = context.getDimensionFromTheme(R.attr.chocolate_BoxButton_TextPaddingBottom)

internal val View.theme_chocolate_box_button_text_style: Int
    get() = context.getInt(R.attr.chocolate_BoxButton_TextStyle, ChocolateTextStyle.Normal.value)

@get:FontRes
internal val View.theme_chocolate_box_button_font_family_res_id_or_zero: Int
    get() = context.getResourceIdOrNullFromTheme(R.attr.chocolate_BoxButton_FontFamily) ?: 0

@get:ColorInt
internal val View.theme_chocolate_box_button_text_color: Int
    get() = context.getColorFromTheme(R.attr.chocolate_BoxButton_TextColor)

@get:ColorInt
internal val View.theme_chocolate_box_button_text_disabled_color: Int
    get() = context.getColorFromTheme(R.attr.chocolate_BoxButton_TextDisabledColor)

internal val View.theme_chocolate_box_button_left_icon_margin_with_text: Float
    get() = context.getDimensionFromTheme(R.attr.chocolate_BoxButton_LeftIconMarginWithText)

internal val View.theme_chocolate_box_button_left_icon_padding: Float
    get() = context.getDimensionFromTheme(R.attr.chocolate_BoxButton_LeftIconPadding)

internal val View.theme_chocolate_box_button_right_icon_margin_with_text: Float
    get() = context.getDimensionFromTheme(R.attr.chocolate_BoxButton_RightIconMarginWithText)

internal val View.theme_chocolate_box_button_right_icon_padding: Float
    get() = context.getDimensionFromTheme(R.attr.chocolate_BoxButton_RightIconPadding)

@get:ColorInt
internal val View.theme_chocolate_box_button_ripple_color: Int
    get() = context.getColorFromTheme(R.attr.chocolate_BoxButton_RippleColor)

@get:ColorInt
internal val View.theme_chocolate_box_button_background_color: Int
    get() = context.getColorFromTheme(R.attr.chocolate_BoxButton_BackgroundColor)

@get:ColorInt
internal val View.theme_chocolate_box_button_background_selected_color: Int
    get() = context.getColorFromTheme(R.attr.chocolate_BoxButton_BackgroundSelectedColor)

@get:ColorInt
internal val View.theme_chocolate_box_button_background_disabled_color: Int
    get() = context.getColorFromTheme(R.attr.chocolate_BoxButton_BackgroundDisabledColor)

internal val View.theme_chocolate_box_button_background_corner_radius: Float
    get() = context.getDimensionFromTheme(R.attr.chocolate_BoxButton_BackgroundCornerRadius)