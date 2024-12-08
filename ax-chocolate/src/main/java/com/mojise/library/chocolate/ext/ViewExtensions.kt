package com.mojise.library.chocolate.ext

import android.view.View

fun View.setPaddingTop(paddingTop: Int) = setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)

fun View.setPaddingBottom(paddingBottom: Int) = setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)

fun View.setPaddingStart(paddingStart: Int) = setPadding(paddingStart, paddingTop, paddingRight, paddingBottom)

fun View.setPaddingEnd(paddingEnd: Int) = setPadding(paddingLeft, paddingTop, paddingEnd, paddingBottom)
