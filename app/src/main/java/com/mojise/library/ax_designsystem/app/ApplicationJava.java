package com.mojise.library.ax_designsystem.app;

import android.app.Application;

import com.mojise.library.ax_designsystem.loading.modal.AxModalLoadingDialog;

public class ApplicationJava extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AxModalLoadingDialog.GlobalOption.setLoadingColorResId(R.color.my_color);
        AxModalLoadingDialog.GlobalOption.setLoadingMessageResId(R.string.my_loading_message);
    }
}