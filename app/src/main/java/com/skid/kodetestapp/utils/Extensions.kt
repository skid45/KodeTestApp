package com.skid.kodetestapp.utils

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.ColorRes
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import java.io.Serializable

fun ViewGroup.autoAnimation(duration: Long) {
    TransitionManager.beginDelayedTransition(
        this,
        AutoTransition().apply {
            setDuration(duration)
        }
    )
}

fun Long.getYearsDeclension(): String {
    val mod100 = this % 100
    val mod10 = this % 10
    return when {
        mod100 in 11L..14L -> "лет"
        mod10 == 1L -> "год"
        mod10 in 2L..4L -> "года"
        else -> "лет"
    }
}

fun Activity.updateStatusBarColor(@ColorRes id: Int) {
    this.window.apply {
        addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        statusBarColor = getColor(id)
    }
}

@Suppress("DEPRECATION")
inline fun <reified T : Serializable> Bundle.customGetSerializable(key: String): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getSerializable(key, T::class.java)
    } else {
        getSerializable(key) as? T
    }
}
