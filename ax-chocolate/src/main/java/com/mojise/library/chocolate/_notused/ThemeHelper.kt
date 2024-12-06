package com.mojise.library.chocolate._notused

//internal object ThemeHelper {
//
//    fun applyTheme(context: Context, themeMode: ChocolateThemeMode) {
//        val uiMode = when (themeMode) {
//            ChocolateThemeMode.DayNight -> Configuration.UI_MODE_NIGHT_UNDEFINED // 기기 설정 따름
//            ChocolateThemeMode.Day -> Configuration.UI_MODE_NIGHT_NO // 라이트 모드 고정
//            ChocolateThemeMode.Night -> Configuration.UI_MODE_NIGHT_YES // 다크 모드 고정
//        }
//
//        // Configuration을 변경하여 테마 적용
//        val configuration = context.resources.configuration
//        configuration.uiMode = configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK.inv() or uiMode
//        context.resources.updateConfiguration(configuration, context.resources.displayMetrics)
//    }
//
//    fun Context.createCustomThemedContext(): Context {
//        val uiMode = when (AxChocolateDesignSystem.themeMode) {
//            ChocolateThemeMode.DayNight -> Configuration.UI_MODE_NIGHT_UNDEFINED
//            ChocolateThemeMode.Day -> Configuration.UI_MODE_NIGHT_NO
//            ChocolateThemeMode.Night -> Configuration.UI_MODE_NIGHT_YES
//        }
//        val newConfiguration = Configuration(resources.configuration)
//        newConfiguration.uiMode = newConfiguration.uiMode and Configuration.UI_MODE_NIGHT_MASK.inv() or uiMode
//        return createConfigurationContext(newConfiguration)
//    }
//}
