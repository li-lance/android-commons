package com.fate.core.libs

import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowInsets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.elevation.ElevationOverlayProvider

/**
 * Author：lance.li on 3/9 0009 00:12
 * email：foryun@live.com
 */

fun Fragment.windowTransparent(window: Window) {
  lifecycleScope.launchWhenStarted {
    window.setBackgroundDrawableResource(android.R.color.transparent)
  }
}

fun View.bindElevationOverlay(previousElevation: Float, elevation: Float) {
  if (previousElevation == elevation) return
  val color = ElevationOverlayProvider(context)
    .compositeOverlayWithThemeSurfaceColorIfNeeded(elevation)
  setBackgroundColor(color)
}

fun View.bindLayoutFullscreen(previousFullscreen: Boolean = false, fullscreen: Boolean = true) {
  if (previousFullscreen != fullscreen && fullscreen) {
    @Suppress("DEPRECATION")
    // The new alternative is WindowCompat.setDecorFitsSystemWindows, but we can't
    // always get access to the window from a view.
    systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
  }
}
//fun View.applyWindowTopInsetAsMargin() {
//
//}

fun View.applyWindowTopInsetAsPadding() {
  applySystemWindowInsetsPadding(
    previousApplyLeft = false,
    previousApplyTop = false,
    previousApplyRight = false,
    previousApplyBottom = false,
    applyLeft = false,
    applyTop = true,
    applyRight = false,
    applyBottom = false
  )
}
fun View.applyWindowTopAndBottomInsetAsPadding() {
  applySystemWindowInsetsPadding(
    previousApplyLeft = false,
    previousApplyTop = false,
    previousApplyRight = false,
    previousApplyBottom = false,
    applyLeft = false,
    applyTop = true,
    applyRight = false,
    applyBottom = true
  )
}

fun View.applySystemWindowInsetsPadding(
  previousApplyLeft: Boolean,
  previousApplyTop: Boolean,
  previousApplyRight: Boolean,
  previousApplyBottom: Boolean,
  applyLeft: Boolean,
  applyTop: Boolean,
  applyRight: Boolean,
  applyBottom: Boolean
) {
  if (previousApplyLeft == applyLeft &&
    previousApplyTop == applyTop &&
    previousApplyRight == applyRight &&
    previousApplyBottom == applyBottom
  ) {
    return
  }

  doOnApplyWindowInsets { view, insets, padding, _ ->
    val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
    val left = if (applyLeft) systemBars.left else 0
    val top = if (applyTop) systemBars.top else 0
    val right = if (applyRight) systemBars.right else 0
    val bottom = if (applyBottom) systemBars.bottom else 0

    view.setPadding(
      padding.left + left,
      padding.top + top,
      padding.right + right,
      padding.bottom + bottom
    )
  }
}

fun View.doOnApplyWindowInsets(
  block: (View, WindowInsetsCompat, InitialPadding, InitialMargin) -> Unit
) {
  // Create a snapshot of the view's padding & margin states
  val initialPadding = recordInitialPaddingForView(this)
  val initialMargin = recordInitialMarginForView(this)
  // Set an actual OnApplyWindowInsetsListener which proxies to the given
  // lambda, also passing in the original padding & margin states
  ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
    block(v, insets, initialPadding, initialMargin)
    // Always return the insets, so that children can also use them
    insets
  }
  // request some insets
  requestApplyInsetsWhenAttached()
}

class InitialPadding(val left: Int, val top: Int, val right: Int, val bottom: Int)

class InitialMargin(val left: Int, val top: Int, val right: Int, val bottom: Int)

private fun recordInitialPaddingForView(view: View) = InitialPadding(
  view.paddingLeft, view.paddingTop, view.paddingRight, view.paddingBottom
)

private fun recordInitialMarginForView(view: View): InitialMargin {
  val lp = view.layoutParams as? ViewGroup.MarginLayoutParams
    ?: throw IllegalArgumentException("Invalid view layout params")
  return InitialMargin(lp.leftMargin, lp.topMargin, lp.rightMargin, lp.bottomMargin)
}

fun View.requestApplyInsetsWhenAttached() {
  if (isAttachedToWindow) {
    // We're already attached, just request as normal
    requestApplyInsets()
  } else {
    // We're not attached to the hierarchy, add a listener to
    // request when we are
    addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
      override fun onViewAttachedToWindow(v: View) {
        v.removeOnAttachStateChangeListener(this)
        v.requestApplyInsets()
      }

      override fun onViewDetachedFromWindow(v: View) = Unit
    })
  }
}