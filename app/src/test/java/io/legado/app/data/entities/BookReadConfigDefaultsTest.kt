package io.legado.app.data.entities

import org.junit.Assert.assertFalse
import org.junit.Test

class BookReadConfigDefaultsTest {

    @Test
    fun `new book read config disables split long chapter by default`() {
        val book = Book()

        assertFalse(book.getSplitLongChapter())
    }
}
