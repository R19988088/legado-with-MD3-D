package io.legado.app.ui.book.read

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
            useFloatingBottomBar: Boolean,
            useFloatingBottomBarLiquidGlass: Boolean,
            liquidGlassAvailable: Boolean,
            baseColor: Int,
            menuAlpha: Int,
            bottomBarBlurAlpha: Int
        ): ReadMenuBottomBarStyle {
            val floating = useFloatingBottomBar
            val liquidGlass = floating && useFloatingBottomBarLiquidGlass && liquidGlassAvailable
            val alpha = if (liquidGlass) {
                bottomBarBlurAlpha.coerceIn(0, 100)
            } else {
                menuAlpha.coerceIn(0, 100)
            }
            return ReadMenuBottomBarStyle(
                floating = floating,
                liquidGlass = liquidGlass,
                backgroundColor = baseColor.withAlpha((alpha / 100f * 255).toInt()),
                cornerRadiusDp = if (floating) 32f else 0f,
                horizontalMarginDp = if (floating) 16 else 0,
                bottomMarginDp = if (floating) 12 else 0,
                elevationDp = if (floating) 8f else 0f
            )
        }

        private fun Int.withAlpha(alpha: Int): Int {
            return (alpha.coerceIn(0, 255) shl 24) or (this and 0x00FFFFFF)
        }
    }
}
