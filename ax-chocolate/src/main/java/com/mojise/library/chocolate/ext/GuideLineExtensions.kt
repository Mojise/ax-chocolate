package com.mojise.library.chocolate.ext

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Guideline

var Guideline.guideBegin: Int
    get() = (layoutParams as ConstraintLayout.LayoutParams).guideBegin
    set(value) {
        setGuidelineBegin(value)
        setGuidelineEnd(-1)
        setGuidelinePercent(-1f)
    }

var Guideline.guideEnd: Int
    get() = (layoutParams as ConstraintLayout.LayoutParams).guideEnd
    set(value) {
        setGuidelineBegin(-1)
        setGuidelineEnd(value)
        setGuidelinePercent(-1f)
    }

var Guideline.guidePercent: Float
    get() = (layoutParams as ConstraintLayout.LayoutParams).guidePercent
    set(value) {
        setGuidelineBegin(-1)
        setGuidelineEnd(-1)
        setGuidelinePercent(value)
    }