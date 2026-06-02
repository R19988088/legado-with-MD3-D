package io.legado.app.ui.widget.components

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FloatingBottomBarConfigTest {

    @Test
    fun `floating and liquid glass use one shared config`() {
        val config = FloatingBottomBarConfig.resolve(
            useFloatingBottomBar = true,
            useFloatingBottomBarLiquidGlass = true,
            blurRadius = 12,
            blurAlpha = 42,
            lensRadius = 18f,
            liquidGlassAvailable = true
        )

        assertTrue(config.floating)
        assertTrue(config.liquidGlass)
        assertEquals(12, config.blurRadius)
        assertEquals(42, config.blurAlpha)
        assertEquals(18f, config.lensRadius)
    }

    @Test
    fun `liquid glass is disabled when floating is disabled`() {
        val config = FloatingBottomBarConfig.resolve(
            useFloatingBottomBar = false,
            useFloatingBottomBarLiquidGlass = true,
            blurRadius = 12,
            blurAlpha = 42,
            lensRadius = 18f,
            liquidGlassAvailable = true
        )

        assertFalse(config.floating)
        assertFalse(config.liquidGlass)
    }

    @Test
    fun `liquid glass follows platform availability`() {
        val config = FloatingBottomBarConfig.resolve(
            useFloatingBottomBar = true,
            useFloatingBottomBarLiquidGlass = true,
            blurRadius = 12,
            blurAlpha = 42,
            lensRadius = 18f,
            liquidGlassAvailable = false
        )

        assertTrue(config.floating)
        assertFalse(config.liquidGlass)
    }

    @Test
    fun `blur alpha is clamped once in shared config`() {
        val config = FloatingBottomBarConfig.resolve(
            useFloatingBottomBar = true,
            useFloatingBottomBarLiquidGlass = true,
            blurRadius = 12,
            blurAlpha = 120,
            lensRadius = 18f,
            liquidGlassAvailable = true
        )

        assertEquals(100, config.blurAlpha)
    }
}
