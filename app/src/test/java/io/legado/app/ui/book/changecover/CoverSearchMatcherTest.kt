package io.legado.app.ui.book.changecover

import io.legado.app.data.entities.SearchBook
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CoverSearchMatcherTest {

    @Test
    fun `matches cover result by name when local book author is empty`() {
        val result = SearchBook(
            name = "本地书",
            author = "网络作者",
            coverUrl = "https://example.com/cover.jpg"
        )

        assertTrue(CoverSearchMatcher.matches(result, name = "本地书", author = ""))
    }

    @Test
    fun `keeps author exact match when local book author is known`() {
        val result = SearchBook(
            name = "本地书",
            author = "其他作者",
            coverUrl = "https://example.com/cover.jpg"
        )

        assertFalse(CoverSearchMatcher.matches(result, name = "本地书", author = "目标作者"))
    }

    @Test
    fun `does not match result without cover`() {
        val result = SearchBook(
            name = "本地书",
            author = "网络作者",
            coverUrl = ""
        )

        assertFalse(CoverSearchMatcher.matches(result, name = "本地书", author = ""))
    }
}
