package com.mojise.library.chocolate.view.model

import com.mojise.library.chocolate.ext.dp
import com.mojise.library.chocolate.view.helper.ChocolateViewHelper.CORNER_RADIUS_NORMAL_PIXEL

data class ChocolateAttribute constructor(
    var isPressEffectEnabled: Boolean,
    var pressEffectScaleRatio: Float,
    var cornerRadius: Float,
    var rippleColors: ChocolateColorState,
    var ripplePosition: DrawablePosition,
    var strokeWidth: Float,
    var strokeColors: ChocolateColorState?,
    var backgroundColors: ChocolateColorState?,
) {

    override fun toString(): String = buildString {
        appendLine("ChocolateAttribute(")
        appendLine("    scaleRatio=$pressEffectScaleRatio")
        appendLine("    cornerRadius=$cornerRadius")
        appendLine("    strokeWidth=$strokeWidth")
        appendLine("    strokeColors=$strokeColors")
        appendLine("    rippleColors=$rippleColors")
        appendLine("    backgroundColors=$backgroundColors")
        appendLine(")")
    }

    companion object {
        val Default = ChocolateAttribute(
            isPressEffectEnabled = true,
            pressEffectScaleRatio = PressEffectStrength.Normal.value,
            cornerRadius = CORNER_RADIUS_NORMAL_PIXEL.dp,
            rippleColors = ChocolateColorState.Transparent,
            ripplePosition = DrawablePosition.Background,
            strokeWidth = 0f,
            strokeColors = ChocolateColorState.Transparent,
            backgroundColors = ChocolateColorState.Transparent,
        )
    }
}