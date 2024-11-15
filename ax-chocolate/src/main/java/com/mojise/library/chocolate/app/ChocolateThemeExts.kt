package com.mojise.library.chocolate.app

import android.content.Context
import android.content.res.Configuration
import android.view.View
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

internal fun Context.getChocolateThemeColor(@ColorRes colorResId: Int): Int {
    return toChocolateThemeModeContext().getColor(colorResId)
}

internal fun AppCompatActivity.getChocolateThemeColor(@ColorRes colorResId: Int): Int {
    return toChocolateThemeModeContext().getColor(colorResId)
}

internal fun Fragment.getChocolateThemeColor(@ColorRes colorResId: Int): Int {
    return requireContext().toChocolateThemeModeContext().getColor(colorResId)
}

internal fun View.getChocolateThemeColor(@ColorRes colorResId: Int): Int {
    return context.toChocolateThemeModeContext().getColor(colorResId)
}

internal fun Context.toChocolateThemeModeContext(): Context {
    val uiMode = when (AxChocolateDesignSystem.themeMode) {
        ChocolateThemeMode.DayNight -> Configuration.UI_MODE_NIGHT_UNDEFINED
        ChocolateThemeMode.Day -> Configuration.UI_MODE_NIGHT_NO
        ChocolateThemeMode.Night -> Configuration.UI_MODE_NIGHT_YES
    }

    val newConfiguration = Configuration(resources.configuration)
    newConfiguration.uiMode = newConfiguration.uiMode and Configuration.UI_MODE_NIGHT_MASK.inv() or uiMode
    return createConfigurationContext(newConfiguration)
}

val Context.isLightTheme: Boolean
    get() = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_NO

val Context.isDarkTheme: Boolean
    get() = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

val Fragment.isLightTheme: Boolean
    get() = requireContext().isLightTheme

val Fragment.isDarkTheme: Boolean
    get() = requireContext().isDarkTheme