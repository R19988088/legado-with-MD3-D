package io.legado.app.ui.book.read

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ReadMenuBottomBarStyleTest {

    @Test
    fun `normal mode keeps full width opaque bottom bar shape`() {
        val style = ReadMenuBottomBarStyle.resolve(
            useFloatingBottomBar = false,
            useFloatingBottomBarLiquidGlass = true,
            liquidGlassAvailable = true,
            baseColor = rgb(10, 20, 30),
            menuAlpha = 80,
            bottomBarBlurAlpha = 40
        )

        assertFalse(style.floating)
        assertFalse(style.liquidGlass)
        assertEquals(0f, style.cornerRadiusDp)
        assertEquals(0, style.horizontalMarginDp)
        assertEquals((80 / 100f * 255).toInt(), alpha(style.backgroundColor))
    }

    @Test
    fun `floating mode uses detached capsule shape`() {
        val style = ReadMenuBottomBarStyle.resolve(
            useFloatingBottomBar = true,
            useFloatingBottomBarLiquidGlass = false,
            liquidGlassAvailable = true,
            baseColor = rgb(10, 20, 30),
            menuAlpha = 80,
            bottomBarBlurAlpha = 40
        )

        assertTrue(style.floating)
        assertFalse(style.liquidGlass)
        assertEquals(32f, style.cornerRadiusDp)
        assertEquals(16, style.horizontalMarginDp)
        assertEquals(12, style.bottomMarginDp)
    }

    @Test
    fun `liquid glass floating mode uses blur alpha`() {
        val style = ReadMenuBottomBarStyle.resolve(
            useFloatingBottomBar = true,
            useFloatingBottomBarLiquidGlass = true,
            liquidGlassAvailable = true,
            baseColor = rgb(10, 20, 30),
            menuAlpha = 80,
            bottomBarBlurAlpha = 40
        )

        assertTrue(style.floating)
        assertTrue(style.liquidGlass)
        assertEquals((40 / 100f * 255).toInt(), alpha(style.backgroundColor))
    }

    @Test
    fun `liquid glass setting falls back when platform cannot use it`() {
        val style = ReadMenuBottomBarStyle.resolve(
            useFloatingBottomBar = true,
            useFloatingBottomBarLiquidGlass = true,
            liquidGlassAvailable = false,
            baseColor = rgb(10, 20, 30),
            menuAlpha = 80,
            bottomBarBlurAlpha = 40
        )

        assertTrue(style.floating)
        assertFalse(style.liquidGlass)
        assertEquals((80 / 100f * 255).toInt(), alpha(style.backgroundColor))
    }

    private fun rgb(red: Int, green: Int, blue: Int): Int {
        return (0xFF shl 24) or (red shl 16) or (green shl 8) or blue
    }

    private fun alpha(color: Int): Int {
        return color ushr 24
    }
}
