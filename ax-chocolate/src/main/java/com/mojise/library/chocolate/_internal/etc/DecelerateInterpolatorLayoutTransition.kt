package com.mojise.library.chocolate._internal.etc

import android.animation.LayoutTransition
import android.view.animation.DecelerateInterpolator

class DecelerateInterpolatorLayoutTransition : LayoutTransition() {

    init {
        setInterpolator(APPEARING, DecelerateInterpolator())
        setInterpolator(DISAPPEARING, DecelerateInterpolator())
        setInterpolator(CHANGING, DecelerateInterpolator())
        setInterpolator(CHANGE_APPEARING, DecelerateInterpolator())
        setInterpolator(CHANGE_DISAPPEARING, DecelerateInterpolator())
    }
}