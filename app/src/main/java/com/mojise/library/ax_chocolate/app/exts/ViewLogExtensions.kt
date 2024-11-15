package com.mojise.library.ax_chocolate.app.exts

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.RestrictTo
import androidx.appcompat.widget.ContentFrameLayout

fun View.findRootView(): View {
    var view = this

    while (view.parent != null && view.parent is View) {
        view = view.parent as View
    }

    return view
}

fun View.logViewTreeFromRootView() {
    val rootView = findRootView()
    var targetViewDepth = Int.MAX_VALUE

    fun dfs(view: View, depth: Int) {
        if (view == this) {
            targetViewDepth = depth
        }
        val logString = "  ".repeat(depth) + view
        when {
            depth == 0 ->
                loge("$logString <- ★root★", prefix = "")
            view == this ->
                loge("$logString <- ★this★", prefix = "")
            targetViewDepth > depth ->
                logw(logString, prefix = "")
            else ->
                logd(logString, prefix = "")
        }
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                dfs(view.getChildAt(i), depth + 1)
            }
        }
    }

    logw("[ViewTree]" + "=".repeat(100), prefix = "")
    dfs(rootView, 0)
    logw("=".repeat(110), prefix = "")
}

fun View.findChildContentFrameLayout(): FrameLayout? {
    var contentFrameLayout: FrameLayout? = null

    fun dfs(view: View) {
        if (view.id == android.R.id.content && view is FrameLayout) {
            contentFrameLayout = view
            return
        }
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                dfs(view.getChildAt(i))
            }
        }
    }

    dfs(this)

    return contentFrameLayout
}