package com.mojise.library.chocolate._internal.theme

import android.view.View
import androidx.annotation.ColorInt
import com.mojise.library.chocolate.R
import com.mojise.library.chocolate._internal.exts.getColorFromTheme

@get:ColorInt
internal val View.theme_chocolate_text_view_text_color: Int
    get() = context.getColorFromTheme(R.attr.chocolate_TextView_TextColor)

@get:ColorInt
internal val View.theme_chocolate_text_view_text_disabled_color: Int
    get() = context.getColorFromTheme(R.attr.chocolate_TextView_TextDisabledColor)