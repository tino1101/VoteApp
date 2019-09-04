package com.tino.vote.widget

import android.view.View

class ViewWrapper(private val mTarget: View) {
    var customWidth: Int
        get() = mTarget.layoutParams.width
        set(width) {
            mTarget.layoutParams.width = width
            mTarget.requestLayout()
        }
}