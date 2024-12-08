package com.mojise.library.chocolate._internal.theme

import androidx.annotation.ColorRes
import androidx.fragment.app.Fragment
import com.mojise.library.chocolate.R
import com.mojise.library.chocolate._internal.exts.getBoolean
import com.mojise.library.chocolate._internal.exts.getDimensionFromTheme
import com.mojise.library.chocolate._internal.exts.getResourceIdFromTheme

internal val Fragment.theme_chocolate_bottom_sheet_outer_margin_enabled: Boolean
    get() = requireActivity().getBoolean(R.attr.chocolate_BottomSheet_OuterMarginEnabled, true)

internal val Fragment.theme_chocolate_bottom_sheet_outer_margin: Float
    get() = requireActivity().getDimensionFromTheme(R.attr.chocolate_BottomSheet_OuterMargin)

internal val Fragment.theme_chocolate_bottom_sheet_corner_radius: Float
    get() = requireActivity().getDimensionFromTheme(R.attr.chocolate_BottomSheet_CornerRadius)

internal val Fragment.theme_chocolate_bottom_sheet_grip_bar_visible: Boolean
    get() = requireActivity().getBoolean(R.attr.chocolate_BottomSheet_GripBarVisible, true)

@get:ColorRes
internal val Fragment.theme_chocolate_bottom_sheet_grip_bar_color: Int
    get() = requireActivity().getResourceIdFromTheme(R.attr.chocolate_BottomSheet_GripBarColor)

@get:ColorRes
internal val Fragment.theme_chocolate_bottom_sheet_background_color: Int
    get() = requireActivity().getResourceIdFromTheme(R.attr.chocolate_BottomSheet_BackgroundColor)