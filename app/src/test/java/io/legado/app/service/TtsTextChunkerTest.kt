package io.legado.app.service

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TtsTextChunkerTest {

    @Test
    fun splitKeepsEveryChunkWithinLimit() {
        val chunks = TtsTextChunker.split("abcdefghij", 3)

        assertEquals(listOf("abc", "def", "ghi", "j"), chunks)
        assertTrue(chunks.all { it.length <= 3 })
    }

    @Test
    fun splitPrefersSentenceBreaks() {
        val chunks = TtsTextChunker.split("第一句。第二句。第三句。", 6)

        assertEquals(listOf("第一句。", "第二句。", "第三句。"), chunks)
        assertTrue(chunks.all { it.length <= 6 })
    }
}
