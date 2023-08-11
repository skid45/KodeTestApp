package com.skid.kodetestapp.utils

import android.view.ViewGroup
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager

fun ViewGroup.autoAnimation(duration: Long) {
    TransitionManager.beginDelayedTransition(
        this,
        AutoTransition().apply {
            setDuration(duration)
        }
    )
}