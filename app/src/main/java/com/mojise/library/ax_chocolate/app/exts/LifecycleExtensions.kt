package com.mojise.library.ax_chocolate.app.exts

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

fun LifecycleOwner.repeatOnCreated(block: suspend CoroutineScope.() -> Unit) {
    lifecycleScope.launch {
        lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED, block)
    }
}

fun LifecycleOwner.repeatOnStarted(block: suspend CoroutineScope.() -> Unit) {
    lifecycleScope.launch {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED, block)
    }
}

fun ViewModel.launch(block: suspend CoroutineScope.() -> Unit): Job {
    return viewModelScope.launch(block = block)
}

fun ViewModel.launched(block: suspend CoroutineScope.() -> Unit) {
    viewModelScope.launch(block = block) // No Return
}

fun ViewModel.launchMain(block: suspend CoroutineScope.() -> Unit): Job {
    return viewModelScope.launch(context = Dispatchers.Main, block = block)
}

fun ViewModel.launchedMain(block: suspend CoroutineScope.() -> Unit) {
    viewModelScope.launch(context = Dispatchers.Main, block = block) // No Return
}

fun ViewModel.launchDefault(block: suspend CoroutineScope.() -> Unit): Job {
    return viewModelScope.launch(context = Dispatchers.Default, block = block)
}

fun ViewModel.launchedDefault(block: suspend CoroutineScope.() -> Unit) {
    viewModelScope.launch(context = Dispatchers.Default, block = block) // No Return
}

fun ViewModel.launchIO(block: suspend CoroutineScope.() -> Unit): Job {
    return viewModelScope.launch(context = Dispatchers.IO, block = block)
}

fun ViewModel.launchedIO(block: suspend CoroutineScope.() -> Unit) {
    viewModelScope.launch(context = Dispatchers.IO, block = block) // No Return
}