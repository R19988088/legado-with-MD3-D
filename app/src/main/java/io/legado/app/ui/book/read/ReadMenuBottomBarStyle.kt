package io.legado.app.ui.book.read

import io.legado.app.ui.widget.components.FloatingBottomBarConfig

data class ReadMenuBottomBarStyle(
    val floating: Boolean,
    val liquidGlass: Boolean,
    val backgroundColor: Int,
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
            val alpha = if (config.liquidGlass) {
                config.blurAlpha
            } else {
                menuAlpha.coerceIn(0, 100)
            }
            return ReadMenuBottomBarStyle(
                floating = config.floating,
                liquidGlass = config.liquidGlass,
                backgroundColor = baseColor.withAlpha((alpha / 100f * 255).toInt()),
                cornerRadiusDp = if (config.floating) 32f else 0f,
                horizontalMarginDp = if (config.floating) 16 else 0,
                bottomMarginDp = if (config.floating) 12 else 0,
                elevationDp = if (config.floating) 8f else 0f
            )
        }

        private fun Int.withAlpha(alpha: Int): Int {
            return (alpha.coerceIn(0, 255) shl 24) or (this and 0x00FFFFFF)
        }
    }
}
