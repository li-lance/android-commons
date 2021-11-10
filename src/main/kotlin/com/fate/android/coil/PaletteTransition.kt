package com.fate.android.coil

import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import coil.annotation.ExperimentalCoilApi
import coil.request.ImageResult
import coil.request.SuccessResult
import coil.transition.Transition
import coil.transition.TransitionTarget
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalCoilApi::class)
class PaletteTransition(
  private val delegate: Transition?,
  private val onGenerated: (Palette) -> Unit
) : Transition {
  override suspend fun transition(target: TransitionTarget, result: ImageResult) {
    // Execute the delegate transition.
    val delegateJob = delegate?.let { delegate ->
      coroutineScope {
        launch(Dispatchers.Main.immediate) {
          delegate.transition(target, result)
        }
      }
    }
    // Compute the palette on a background thread.
    if (result is SuccessResult) {
      val bitmap = result.drawable.toBitmap()
      val palette = withContext(Dispatchers.IO) {
        Palette.Builder(bitmap).generate()
      }
      onGenerated(palette)
    }
    delegateJob?.join()
  }
}
