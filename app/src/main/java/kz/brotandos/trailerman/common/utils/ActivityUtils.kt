package kz.brotandos.trailerman.common.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewAnimationUtils
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.appbar.CollapsingToolbarLayout
import kz.brotandos.trailerman.R
import kz.brotandos.trailerman.common.show

fun requestGlideListener(view: View): RequestListener<Drawable> = object : RequestListener<Drawable> {
    override fun onLoadFailed(
        e: GlideException?,
        model: Any?,
        target: Target<Drawable>?,
        isFirstResource: Boolean
    ): Boolean = false

    override fun onResourceReady(
        resource: Drawable?,
        model: Any?,
        target: Target<Drawable>?,
        dataSource: DataSource?,
        isFirstResource: Boolean
    ): Boolean {
        val cx = (view.left + view.right) / 2
        val cy = (view.top + view.bottom) / 2
        val finalRadius = Math.max(view.width, view.height)

        if (view.isAttachedToWindow) {
            val anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0f, finalRadius.toFloat())
            view.show()
            view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.background))
            anim.duration = 550
            anim.start()
        }
        return false
    }
}

fun applyToolbarMargin(context: Context, toolbar: Toolbar) {
    toolbar.layoutParams = (toolbar.layoutParams as CollapsingToolbarLayout.LayoutParams).apply {
        val statusBarHeightId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        topMargin = if (statusBarHeightId > 0) context.resources.getDimensionPixelSize(statusBarHeightId) else 0
    }
}