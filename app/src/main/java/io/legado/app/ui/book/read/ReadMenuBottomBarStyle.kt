package io.legado.app.ui.book.read

import androidx.core.graphics.ColorUtils
import io.legado.app.ui.widget.components.FloatingBottomBarConfig

data class ReadMenuBottomBarStyle(
    val floating: Boolean,
    val liquidGlass: Boolean,
    val backgroundColor: Int,
    val strokeColor: Int,
    val strokeWidthDp: Int,
    val cornerRadiusDp: Float,
    val horizontalMarginDp: Int,
    val bottomMarginDp: Int,
    val elevationDp: Float
) {
    companion object {
        fun resolve(
            config: FloatingBottomBarConfig,
            baseColor: Int,
            menuAlpha: Int
        ): ReadMenuBottomBarStyle {
            val alphaPercent = if (config.liquidGlass) {
                config.blurAlpha
            } else {
                menuAlpha.coerceIn(0, 100)
            }
            val alpha = (alphaPercent / 100f * 255).toInt()
            return ReadMenuBottomBarStyle(
                floating = config.floating,
                liquidGlass = config.liquidGlass,
                backgroundColor = ColorUtils.setAlphaComponent(baseColor, alpha),
                strokeColor = ColorUtils.setAlphaComponent(
                    baseColor,
                    if (config.liquidGlass) 72 else 0
                ),
                strokeWidthDp = if (config.liquidGlass) 1 else 0,
                cornerRadiusDp = if (config.floating) 36f else 0f,
                horizontalMarginDp = if (config.floating) 16 else 0,
                bottomMarginDp = if (config.floating) 12 else 0,
                elevationDp = if (config.liquidGlass) 10f else if (config.floating) 8f else 0f
            )
        }
    }
}
