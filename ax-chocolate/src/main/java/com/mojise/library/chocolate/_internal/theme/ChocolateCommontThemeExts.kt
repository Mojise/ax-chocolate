package com.mojise.library.chocolate._internal.theme

import android.view.View
import androidx.annotation.ColorInt
import com.mojise.library.chocolate.R
import com.mojise.library.chocolate._internal.exts.getBoolean
import com.mojise.library.chocolate._internal.exts.getColorFromTheme
import com.mojise.library.chocolate._internal.exts.getInt
import com.mojise.library.chocolate.view.model.PressEffectStrength

internal val View.theme_chocolate_press_effect_enabled: Boolean
    get() = context.getBoolean(R.attr.chocolate_PressEffectEnabled, true)

internal val View.theme_chocolate_press_effect_strength_level: Int
    get() = context.getInt(R.attr.chocolate_PressEffectStrengthLevel, PressEffectStrength.Deep.level)

@get:ColorInt
internal val View.theme_chocolate_ripple_color: Int
    get() = context.getColorFromTheme(R.attr.chocolate_RippleColor)

@get:ColorInt
internal val View.theme_chocolate_background_disabled_color: Int
    get() = context.getColorFromTheme(R.attr.chocolate_BackgroundDisabledColor)