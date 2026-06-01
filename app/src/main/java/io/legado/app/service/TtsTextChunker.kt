package io.legado.app.service

internal object TtsTextChunker {

    fun split(text: String, maxLength: Int): List<String> {
        if (text.isEmpty()) return emptyList()
        val limit = maxLength.coerceAtLeast(1)
        if (text.length <= limit) return listOf(text)
        val chunks = arrayListOf<String>()
        var start = 0
        while (start < text.length) {
            var end = minOf(start + limit, text.length)
            if (end < text.length) {
                val breakIndex = findBreakIndex(text, start, end)
                if (breakIndex > start) {
                    end = breakIndex
                }
            }
            chunks.add(text.substring(start, end))
            start = end
        }
        return chunks
    }

    private fun findBreakIndex(text: String, start: Int, end: Int): Int {
        for (index in end - 1 downTo start + 1) {
            val char = text[index]
            if (char.isWhitespace() || char in breakChars) {
                return index + 1
            }
        }
        return end
    }

    private val breakChars = setOf(
        '.', ',', ';', ':', '!', '?',
        '\u3002', '\uff0c', '\uff1b', '\uff1a', '\uff01', '\uff1f',
        '\u3001'
    )
}
