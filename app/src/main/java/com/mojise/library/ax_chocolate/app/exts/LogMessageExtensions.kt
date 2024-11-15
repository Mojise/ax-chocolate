package com.mojise.library.ax_chocolate.app.exts

import android.util.Log

private val Any.currentClassName: String
    get() = javaClass.simpleName

fun Any.log(
    message: Any? = "",
    throwable: Throwable? = null,
    additionalMethodDepth: Int = 0,
    prefix: String = prefix(additionalMethodDepth),
) {
    logd(message.toString(), throwable, additionalMethodDepth + 1, prefix)
}

fun Any.logd(
    message: Any? = "",
    throwable: Throwable? = null,
    additionalMethodDepth: Int = 0,
    prefix: String = prefix(additionalMethodDepth),
) {
    Log.d(currentClassName, prefix + message.toString(), throwable)
}

fun Any.logi(
    message: Any? = "",
    throwable: Throwable? = null,
    additionalMethodDepth: Int = 0,
    prefix: String = prefix(additionalMethodDepth),
) {
    Log.i(currentClassName, prefix + message.toString(), throwable)
}

fun Any.logw(
    message: Any? = "",
    throwable: Throwable? = null,
    additionalMethodDepth: Int = 0,
    prefix: String = prefix(additionalMethodDepth),
) {
    Log.w(currentClassName, prefix + message.toString(), throwable)
}

fun Any.loge(
    message: Any? = "",
    throwable: Throwable? = null,
    additionalMethodDepth: Int = 0,
    prefix: String = prefix(additionalMethodDepth),
) {
    Log.e(currentClassName, prefix + message.toString(), throwable)
}

private fun prefix(additionalMethodDepth: Int): String {
    return "${getCurrentMethodName(additionalMethodDepth)} :: "
}

private fun getCurrentMethodName(additionalMethodDepth: Int): String {
    return Thread.currentThread().stackTrace[5 + additionalMethodDepth].run { "[$fileName:$lineNumber]$methodName()" }
}