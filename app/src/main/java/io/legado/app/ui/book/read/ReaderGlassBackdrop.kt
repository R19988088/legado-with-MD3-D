package io.legado.app.ui.book.read

import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

fun View.captureBackdropBitmap(): ImageBitmap? {
    if (width <= 0 || height <= 0) return null
    val root = rootView ?: return null
    if (root.width <= 0 || root.height <= 0) return null

    val targetLocation = IntArray(2)
    val rootLocation = IntArray(2)
    getLocationOnScreen(targetLocation)
    root.getLocationOnScreen(rootLocation)

    val oldAlpha = alpha
    alpha = 0f
    return try {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.translate(
            (rootLocation[0] - targetLocation[0]).toFloat(),
            (rootLocation[1] - targetLocation[1]).toFloat()
        )
        root.draw(canvas)
        bitmap.asImageBitmap()
    } finally {
        alpha = oldAlpha
    }
}
