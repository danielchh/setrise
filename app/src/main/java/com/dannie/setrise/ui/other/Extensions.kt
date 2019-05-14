package com.dannie.setrise.ui.other

import android.view.animation.Animation

fun Animation.onAnimationStart(onAnimationStart: () -> Unit) {
    this.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationRepeat(p0: Animation?) {}

        override fun onAnimationEnd(p0: Animation?) {}

        override fun onAnimationStart(p0: Animation?) {
            onAnimationStart()
        }
    })
}

fun Animation.onAnimationEnd(onAnimationEnd: () -> Unit) {
    this.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationRepeat(p0: Animation?) {}

        override fun onAnimationEnd(p0: Animation?) {
            onAnimationEnd()
        }

        override fun onAnimationStart(p0: Animation?) {}
    })
}