package com.mojise.library.chocolate.view.model

enum class DrawablePosition(val value: Int) {
    Foreground(value = 0),
    Background(value = 1);

    companion object {
        fun valueOf(value: Int): DrawablePosition = entries.firstOrNull { it.value == value } ?: Background
    }
}