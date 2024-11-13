package com.mojise.library.ax_chocolate.app

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.children
import com.mojise.library.ax_chocolate.loading.modal.AxModalLoadingDialog2
import com.mojise.library.ax_chocolate.loading.modal.axModalLoadingDialog
import kotlin.concurrent.timer

class MainActivityKotlin : AppCompatActivity() {

    private val loadingDialog by axModalLoadingDialog()
    private val loadingDialog2 = AxModalLoadingDialog2(this)
    private val loadingDialog2Getter: AxModalLoadingDialog2
        get() = loadingDialog2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initButtons()
    }

    private fun initButtons() {
        findViewById<TextView>(R.id.title)?.text = javaClass.simpleName
        findViewById<Button>(R.id.btn_show_loading_1)?.setOnClickListener(::onClicked)
        findViewById<Button>(R.id.btn_show_loading_2)?.setOnClickListener(::onClicked)
        findViewById<Button>(R.id.btn_show_loading_3)?.setOnClickListener(::onClicked)
        findViewById<Button>(R.id.btn_show_loading_4)?.setOnClickListener(::onClicked)
        findViewById<Button>(R.id.btn_show_loading_5)?.setOnClickListener(::onClicked)
        findViewById<Button>(R.id.btn_show_loading_6)?.setOnClickListener(::onClicked)
        findViewById<Button>(R.id.btn_show_loading_7)?.setOnClickListener(::onClicked)
        findViewById<Button>(R.id.btn_show_loading_8)?.setOnClickListener(::onClicked)

        findViewById<ViewGroup>(R.id.button_container)
            ?.children
            ?.forEach { it.setOnClickListener(::onClicked) }
    }

    private fun onClicked(view: View) { when (view.id) {
        R.id.btn_show_loading_1 -> {

        }
        R.id.btn_show_loading_2 -> {

        }
        R.id.btn_show_loading_3 -> {

        }
        R.id.btn_show_loading_4 -> {

        }
        R.id.btn_show_loading_5 -> {

        }
        R.id.btn_show_loading_6 -> {

        }
        R.id.btn_show_loading_7 -> {

        }
        R.id.btn_show_loading_8 -> {

        }
    }}

    private fun postDelayed(delayMillis: Long = 3000L, action: () -> Unit) {
        Handler(Looper.getMainLooper()).postDelayed(action, delayMillis)
    }

    companion object {
        private const val TAG = "MainActivityKotlin"
    }
}