package io.legado.app.utils

import org.junit.Assert.assertEquals
import org.junit.Test

class ReadRecordTimeFormatterTest {

    @Test
    fun `book info read duration uses hours above one hour`() {
        assertEquals("已经阅读 1.6小时", formatBookInfoReadDuration(96 * 60 * 1000L))
    }

    @Test
    fun `book info read duration uses minutes above one minute`() {
        assertEquals("已经阅读 12分钟", formatBookInfoReadDuration(12 * 60 * 1000L))
    }

    @Test
    fun `book info read duration uses seconds below one minute`() {
        assertEquals("已经阅读 35秒", formatBookInfoReadDuration(35 * 1000L))
    }
}
