package com.mojise.library.ax_chocolate.app

import android.app.Application
import com.mojise.library.chocolate.app.AxChocolateDesignSystem
import com.mojise.library.chocolate.app.ChocolateThemeMode
import com.mojise.library.chocolate.modal.AxModalLoadingDialog

class ApplicationKotlin : Application() {

    override fun onCreate() {
        super.onCreate()

        AxModalLoadingDialog.GlobalOption.loadingColorResId = R.color.my_color
        AxModalLoadingDialog.GlobalOption.loadingMessageResId = R.string.my_loading_message

        // Ax-Chocolate 디자인 시스템 설정
        with (AxChocolateDesignSystem) {
            // 테마 모드 설정
            themeMode = ChocolateThemeMode.Night

            // 컬러 설정
            primaryColor = R.color.my_primary_color
            secondaryColor = R.color.my_secondary_color
            tertiaryColor = R.color.my_tertiary_color
        }
    }
}