package com.mojise.library.ax_designsystem.app

import android.content.res.ColorStateList
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mojise.library.ax_designsystem.ext.getColorFromTheme
import com.mojise.library.ax_designsystem.loading.modal.AxModalLoadingStyle
import com.mojise.library.ax_designsystem.loading.modal.axModalLoadingDialog
import kotlin.concurrent.timer

class MainActivityKotlin : AppCompatActivity() {

    private val loadingDialog by axModalLoadingDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<TextView>(R.id.title)?.text = javaClass.simpleName
        findViewById<Button>(R.id.btn_show_loading_1)?.onClickedShowLoading()
        findViewById<Button>(R.id.btn_show_loading_2)?.let {
            it.backgroundTintList =
                ColorStateList.valueOf(getColorFromTheme(android.R.attr.colorAccent))
        }
    }

    private fun Button.onClickedShowLoading() {
        setOnClickListener {
            loadingDialog.show()
            Handler(Looper.getMainLooper()).postDelayed({
                loadingDialog.hide()
            }, 3000)
        }
    }
}