package com.mojise.library.chocolate.ext

import android.os.Build

//inline fun <T : AutoCloseable?, R> T.useCompat(block: (T) -> R): R {
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//        return use(block)
//    } else {
//        try {
//            return block(this)
//        } finally {
//            try {
//                close()
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }
//}