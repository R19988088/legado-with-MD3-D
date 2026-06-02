package io.legado.app.ui.book.changecover

import io.legado.app.data.entities.SearchBook

object CoverSearchMatcher {

    fun matches(book: SearchBook, name: String, author: String): Boolean {
        val normalizedAuthor = author.trim()
        return book.name == name &&
                !book.coverUrl.isNullOrEmpty() &&
                (normalizedAuthor.isEmpty() || book.author == normalizedAuthor)
    }
}
