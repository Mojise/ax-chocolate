package com.mojise.library.ax_chocolate.app

import android.app.Application
import com.mojise.library.chocolate.app.AxChocolateDesignSystem
import com.mojise.library.chocolate.ext.sp
import com.mojise.library.chocolate.modal.AxModalLoadingDialog

class ApplicationKotlin : Application() {

    override fun onCreate() {
        super.onCreate()

        AxModalLoadingDialog.GlobalOption.loadingColorResId = R.color.my_color
        AxModalLoadingDialog.GlobalOption.loadingMessageResId = R.string.my_loading_message

        setupAxChocolateDesignSystem()
    }

    private fun setupAxChocolateDesignSystem() {
        // Ax-Chocolate 디자인 설정
        AxChocolateDesignSystem.let {
            // UiMode 설정 (Day, Night, DayNight)
            it.setUiThemeDayNightMode(this)

            it.primaryColorResId = R.color.my_primary_color
            it.secondaryColorResId = R.color.my_secondary_color
            it.tertiaryColorResId = R.color.my_tertiary_color
        }
        // BoxButton 디자인 설정
        AxChocolateDesignSystem.BoxButton.let {
            it.textSize = 18f.sp
//            it.textColorResId = R.color.my_box_button_text_color
//            it.textDisabledColorResId = R.color.my_box_button_text_disabled_color
//            it.backgroundColorResId = R.color.my_box_button_background_color
//            it.backgroundDisabledColorResId = R.color.my_box_button_background_disabled_color
        }
    }
}