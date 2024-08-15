package com.mojise.library.ax_designsystem.app

import android.app.Application
import com.mojise.library.ax_designsystem.loading.modal.AxModalLoadingDialog

class ApplicationKotlin : Application() {

    override fun onCreate() {
        super.onCreate()

        AxModalLoadingDialog.GlobalOption.loadingColorResId = R.color.my_color
        AxModalLoadingDialog.GlobalOption.loadingMessageResId = R.string.my_loading_message
    }
}