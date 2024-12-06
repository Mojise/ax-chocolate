package com.mojise.library.chocolate._notused

//internal val Context.primaryColor: Int
//    get() = getChocolateThemeColor(AxChocolateDesignSystem.primaryColor)
//
//internal val View.primaryColor: Int
//    get() = getChocolateThemeColor(AxChocolateDesignSystem.primaryColor)
//
//internal val Context.rippleColor: Int
//    get() = getChocolateThemeColor(R.color.chocolate_ripple_color_black)
//
//internal val View.chocolateButtonTextColor: Int
//    get() = getChocolateThemeColor(AxChocolateDesignSystem.BoxButton.textColor)
//
//internal val View.chocolateButtonTextDisabledColor: Int
//    get() = getChocolateThemeColor(AxChocolateDesignSystem.BoxButton.textDisabledColor)
//
//internal val View.chocolateButtonBackgroundDisabledColor: Int
//    get() = getChocolateThemeColor(R.color.chocolate_button_background_disabled)
//
//internal fun Context.getChocolateThemeColor(@ColorRes colorResId: Int): Int {
//    return toChocolateThemeModeContext().getColor(colorResId)
//}
//
//internal fun AppCompatActivity.getChocolateThemeColor(@ColorRes colorResId: Int): Int {
//    return toChocolateThemeModeContext().getColor(colorResId)
//}
//
//internal fun Fragment.getChocolateThemeColor(@ColorRes colorResId: Int): Int {
//    return requireContext().toChocolateThemeModeContext().getColor(colorResId)
//}
//
//internal fun View.getChocolateThemeColor(@ColorRes colorResId: Int): Int {
//    return context.toChocolateThemeModeContext().getColor(colorResId)
//}
//
//internal fun Context.toChocolateThemeModeContext(): Context {
//    val uiMode = when (AxChocolateDesignSystem.themeMode) {
//        ChocolateThemeMode.DayNight -> Configuration.UI_MODE_NIGHT_UNDEFINED
//        ChocolateThemeMode.Day -> Configuration.UI_MODE_NIGHT_NO
//        ChocolateThemeMode.Night -> Configuration.UI_MODE_NIGHT_YES
//    }
//
//    val newConfiguration = Configuration(resources.configuration)
//    newConfiguration.uiMode = newConfiguration.uiMode and Configuration.UI_MODE_NIGHT_MASK.inv() or uiMode
//    return createConfigurationContext(newConfiguration)
//}
//
//val Context.isLightTheme: Boolean
//    get() = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_NO
//
//val Context.isDarkTheme: Boolean
//    get() = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
//
//val Fragment.isLightTheme: Boolean
//    get() = requireContext().isLightTheme
//
//val Fragment.isDarkTheme: Boolean
//    get() = requireContext().isDarkTheme
//
//fun Int.foo() {
//
//}