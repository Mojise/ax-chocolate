package com.mojise.library.ax_chocolate.app.exts

import android.view.View

inline fun View.setOnClickListener(crossinline action: () -> Unit = {}) {
    setOnClickListener { action() }
}