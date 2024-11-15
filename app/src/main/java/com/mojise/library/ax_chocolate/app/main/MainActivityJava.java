package com.mojise.library.ax_chocolate.app.main;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mojise.library.ax_chocolate.app.R;
import com.mojise.library.chocolate.modal.AxModalLoadingDialog;

public class MainActivityJava extends AppCompatActivity {

    private final AxModalLoadingDialog loadingDialog = new AxModalLoadingDialog(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
