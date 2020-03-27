package com.example.drawdemo

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.res.ResourcesCompat

class GifImage @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {


    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        if (visibility == View.VISIBLE) {
            background = ResourcesCompat.getDrawable(context.resources, R.drawable.gif_play, null)
            val frameAnimation = background as AnimationDrawable
            frameAnimation.start()
        } else {
            releaseResource()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        releaseResource()
    }

    private fun releaseResource() {
        if (background is AnimationDrawable?) {
            val frameAnimation = background as AnimationDrawable?
            frameAnimation?.stop()
        }
        setImageDrawable(null)
        background = null
    }
}