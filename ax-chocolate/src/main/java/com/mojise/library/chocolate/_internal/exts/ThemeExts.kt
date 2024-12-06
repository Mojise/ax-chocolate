package com.mojise.library.chocolate._internal.exts

import androidx.annotation.ColorRes
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment

internal fun Fragment.getColor(@ColorRes colorResId: Int) =
    ResourcesCompat.getColor(resources, colorResId, requireContext().theme)