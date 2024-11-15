package com.mojise.library.ax_chocolate.app.exts

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

fun Activity.showToast(message: String?, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun Activity.showToast(@StringRes stringResId: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, getString(stringResId), duration).show()
}

fun Fragment.showToast(message: String?, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(requireActivity(), message, duration).show()
}

fun Fragment.showToast(@StringRes stringResId: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(requireActivity(), getString(stringResId), duration).show()
}

fun Context.showToast(message: String?, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun Context.showToast(@StringRes stringResId: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, getString(stringResId), duration).show()
}

fun Context.showSoftInput(editText: EditText) {
    editText.requestFocus()
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
}

fun Context.hideSoftInput(editText: EditText) {
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(editText.windowToken, 0)
}

inline fun <reified T : Activity> Activity.startActivityTo(
    intentBuilder: Intent.() -> Unit = { },
    withFinish: Boolean = false,
    withSingleTopFlag: Boolean = true,
) {
    val intent = Intent(this, T::class.java)
        .apply(intentBuilder)

    if (withSingleTopFlag) {
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
    }

    startActivity(intent)

    if (withFinish) {
        finish()
    }
}