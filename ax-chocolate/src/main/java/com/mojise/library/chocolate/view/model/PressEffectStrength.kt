package com.mojise.library.chocolate.view.model

internal enum class PressEffectStrength(val level: Int, val value: Float) {
    Soft        (level = 0, value = 0.98f), // 눌림 효과 작음
    Normal      (level = 1, value = 0.96f), // 눌림 효과 적당함 (Default)
    Deep        (level = 2, value = 0.94f), // 눌림 효과 큼
    DeepStrong  (level = 3, value = 0.92f), // 눌림 효과 매우 큼
    DeepExtreme (level = 4, value = 0.90f); // 눌림 효과 상당히 큼

    companion object {
        val Default = Normal
        fun valueOf(level: Int): PressEffectStrength = entries.first { it.level == level }
    }
}