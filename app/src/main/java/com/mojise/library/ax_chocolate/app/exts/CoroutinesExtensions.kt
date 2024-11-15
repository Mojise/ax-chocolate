package com.mojise.library.ax_chocolate.app.exts

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun <T> withContextMain(
    block: suspend CoroutineScope.() -> T
): T = withContext(Dispatchers.Main, block)

suspend fun <T> withContextDefault(
    block: suspend CoroutineScope.() -> T
): T = withContext(Dispatchers.Default, block)

suspend fun <T> withContextIO(
    block: suspend CoroutineScope.() -> T
): T = withContext(Dispatchers.IO, block)