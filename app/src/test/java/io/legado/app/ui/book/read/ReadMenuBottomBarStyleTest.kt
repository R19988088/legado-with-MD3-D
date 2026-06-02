package io.legado.app.ui.book.read

import io.legado.app.ui.widget.components.FloatingBottomBarConfig
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ReadMenuBottomBarStyleTest {

    @Test
    fun `normal mode keeps full width bottom bar using menu alpha`() {
        val style = ReadMenuBottomBarStyle.resolve(
            config = sharedConfig(floating = false, liquidGlass = false),
            baseColor = rgb(10, 20, 30),
            menuAlpha = 80
        )

        assertFalse(style.floating)
        assertFalse(style.liquidGlass)
        assertEquals(0f, style.cornerRadiusDp)
        assertEquals(0, style.horizontalMarginDp)
        assertEquals(0, alpha(style.strokeColor))
        assertEquals((80 / 100f * 255).toInt(), alpha(style.backgroundColor))
    }

    @Test
    fun `floating mode uses detached capsule shape`() {
        val style = ReadMenuBottomBarStyle.resolve(
            config = sharedConfig(floating = true, liquidGlass = false),
            baseColor = rgb(10, 20, 30),
            menuAlpha = 80
        )

        assertTrue(style.floating)
        assertFalse(style.liquidGlass)
        assertEquals(32f, style.cornerRadiusDp)
        assertEquals(16, style.horizontalMarginDp)
        assertEquals(12, style.bottomMarginDp)
        assertEquals(0, alpha(style.strokeColor))
    }

    @Test
    fun `liquid glass floating mode uses blur alpha and highlight stroke`() {
        val style = ReadMenuBottomBarStyle.resolve(
            config = sharedConfig(floating = true, liquidGlass = true),
            baseColor = rgb(10, 20, 30),
            menuAlpha = 80
        )

        assertTrue(style.floating)
        assertTrue(style.liquidGlass)
        assertEquals((40 / 100f * 255).toInt(), alpha(style.backgroundColor))
        assertEquals(1, style.strokeWidthDp)
        assertEquals(90, alpha(style.strokeColor))
        assertEquals(12f, style.elevationDp)
    }

    @Test
    fun `liquid glass setting falls back when platform cannot use it`() {
        val style = ReadMenuBottomBarStyle.resolve(
            config = sharedConfig(floating = true, liquidGlass = false),
            baseColor = rgb(10, 20, 30),
            menuAlpha = 80
        )

        assertTrue(style.floating)
        assertFalse(style.liquidGlass)
        assertEquals(0, alpha(style.strokeColor))
        assertEquals((80 / 100f * 255).toInt(), alpha(style.backgroundColor))
    }

    private fun sharedConfig(floating: Boolean, liquidGlass: Boolean): FloatingBottomBarConfig {
        return FloatingBottomBarConfig(
            floating = floating,
            liquidGlass = liquidGlass,
            blurRadius = 8,
            blurAlpha = 40,
            lensRadius = 24f
        )
    }

    private fun rgb(red: Int, green: Int, blue: Int): Int {
        return (0xFF shl 24) or (red shl 16) or (green shl 8) or blue
    }

    private fun alpha(color: Int): Int {
        return color ushr 24
    }
}
