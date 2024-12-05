package com.mojise.library.chocolate.view.model

internal enum class PressEffectStrength(val level: Int, val value: Float) {
    LightTiny    (level = 0, value = 0.98f),
    Light        (level = 1, value = 0.96f),
    Normal       (level = 2, value = 0.94f),
    Heavy        (level = 3, value = 0.92f),
    HeavyStrong  (level = 4, value = 0.90f),
    HeavyExtreme (level = 5, value = 0.85f);

    companion object {
        fun valueOf(level: Int): PressEffectStrength? = entries.firstOrNull { it.level == level }
    }
}