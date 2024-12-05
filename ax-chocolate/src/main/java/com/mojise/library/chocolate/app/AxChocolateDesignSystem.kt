package com.mojise.library.chocolate.app

import androidx.annotation.ColorRes
import com.mojise.library.chocolate.R
import com.mojise.library.chocolate.ext.sp

object AxChocolateDesignSystem {

    /**
     * 테마 모드 설정
     * - 기본 값은 [ChocolateThemeMode.Day]
     *
     * @see ChocolateThemeMode
     */
    var themeMode: ChocolateThemeMode = ChocolateThemeMode.Day

    @ColorRes
    var primaryColor: Int = R.color.chocolate_primary_color
    @ColorRes
    var secondaryColor: Int = R.color.chocolate_primary_color
    @ColorRes
    var tertiaryColor: Int = R.color.chocolate_primary_color

    object BoxButton {
        var textSize: Float = 18f.sp
        @ColorRes
        var textColor: Int = R.color.chocolate_white
    }
}