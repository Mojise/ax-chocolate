package com.mojise.library.chocolate.view.model

enum class ChocolateTextStyle(val value: Int) {
    Normal(0),
    Bold(1),
    Italic(2),
    BoldItalic(3);

    companion object {
        fun valueOf(value: Int): ChocolateTextStyle = entries.first { it.value == value }
    }
}