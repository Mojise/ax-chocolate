package com.mojise.library.chocolate.app

import android.app.Application
import android.app.UiModeManager
import android.os.Build
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatDelegate
import com.mojise.library.chocolate.R
import com.mojise.library.chocolate.button.box.ChocolateBoxButton
import com.mojise.library.chocolate.ext.sp

object AxChocolateDesignSystem {

    private enum class UiMode {
        Day, Night, DayNight
    }

    @ColorRes
    var primaryColorResId: Int = R.color.chocolate_primary_color
    @ColorRes
    var secondaryColorResId: Int = R.color.chocolate_secondary_color
    @ColorRes
    var tertiaryColorResId: Int = R.color.chocolate_tertiary_color

    /**
     * [ChocolateBoxButton]의 디자인 시스템 설정
     */
    object BoxButton {
        /** [ChocolateBoxButton]의 텍스크 크기 (단위: px) */
        var textSize: Float = 18f.sp

        /** [ChocolateBoxButton]의 텍스크 색상 Resource ID */
        @ColorRes
        var textColorResId: Int = R.color.chocolate_button_text

        /** [ChocolateBoxButton]의 비활성화된 텍스크 색상 Resource ID */
        @ColorRes
        var textDisabledColorResId: Int = R.color.chocolate_button_text_disabled

        /** [ChocolateBoxButton]의 배경 색상 Resource ID */
        @ColorRes
        var backgroundColorResId: Int = R.color.chocolate_button_background

        /** [ChocolateBoxButton]의 비활성화된 배경 색상 Resource ID */
        @ColorRes
        var backgroundDisabledColorResId: Int = R.color.chocolate_button_background_disabled
    }

    /** Application 수준에서 Day UI 모드로 설정 */
    fun setUiThemeDayMode(application: Application) {
        setUiMode(application, UiMode.Day)
    }

    /** Application 수준에서 Night UI 모드로 설정 */
    fun setUiThemeNightMode(application: Application) {
        setUiMode(application, UiMode.Night)
    }

    /** Application 수준에서 Day or Night UI 모드로 설정 (시스템 설정에 따라감) */
    fun setUiThemeDayNightMode(application: Application) {
        setUiMode(application, UiMode.DayNight)
    }

    /**
     * @see <a href="https://developer.android.com/develop/ui/views/theming/darktheme?hl=ko#change-themes">Android Developers - 앱 내에서 테마 변경</a>
     */
    private fun setUiMode(application: Application, uiMode: UiMode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val uiModeManager = application.getSystemService(UiModeManager::class.java)
            uiModeManager.setApplicationNightMode(when (uiMode) {
                UiMode.Day -> UiModeManager.MODE_NIGHT_NO
                UiMode.Night -> UiModeManager.MODE_NIGHT_YES
                UiMode.DayNight -> UiModeManager.MODE_NIGHT_AUTO
            })
        } else {
            AppCompatDelegate.setDefaultNightMode(when (uiMode) {
                UiMode.Day -> AppCompatDelegate.MODE_NIGHT_NO
                UiMode.Night -> AppCompatDelegate.MODE_NIGHT_YES
                UiMode.DayNight -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            })
        }
    }
}