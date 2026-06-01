package io.legado.app.ui.widget.components.image.cover

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class CoverPathNormalizerTest {

    @Test
    fun `blank cover path normalizes to null`() {
        assertNull(normalizeCoverPath(null))
        assertNull(normalizeCoverPath(""))
        assertNull(normalizeCoverPath("   "))
    }

    @Test
    fun `use default cover sentinel normalizes to null`() {
        assertNull(normalizeCoverPath("use_default_cover"))
    }

    @Test
    fun `regular cover path stays unchanged`() {
        assertEquals("https://example.com/cover.jpg", normalizeCoverPath("https://example.com/cover.jpg"))
    }
}
