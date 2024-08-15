package com.mojise.library.ax_designsystem.app;

import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mojise.library.ax_designsystem.loading.modal.AxModalLoadingDialog;

public class MainActivityJava extends AppCompatActivity {

    private final AxModalLoadingDialog loadingDialog = new AxModalLoadingDialog(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_show_loading_1).setOnClickListener(v -> {
            loadingDialog.show();

            new Handler().postDelayed(loadingDialog::hide, 3000);
        });
    }
}
