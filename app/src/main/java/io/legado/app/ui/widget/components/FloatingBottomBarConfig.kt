package io.legado.app.ui.widget.components

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast

data class FloatingBottomBarConfig(
    val floating: Boolean,
    val liquidGlass: Boolean,
    val blurRadius: Int,
    val blurAlpha: Int,
    val lensRadius: Float
) {
    companion object {

        fun resolve(
            useFloatingBottomBar: Boolean,
            useFloatingBottomBarLiquidGlass: Boolean,
            blurRadius: Int,
            blurAlpha: Int,
            lensRadius: Float,
            liquidGlassAvailable: Boolean = isLiquidGlassAvailable()
        ): FloatingBottomBarConfig {
            val floating = useFloatingBottomBar
            return FloatingBottomBarConfig(
                floating = floating,
                liquidGlass = floating && useFloatingBottomBarLiquidGlass && liquidGlassAvailable,
                blurRadius = blurRadius,
                blurAlpha = blurAlpha.coerceIn(0, 100),
                lensRadius = lensRadius
            )
        }

        @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.TIRAMISU)
        fun isLiquidGlassAvailable(): Boolean {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
        }
    }
}
