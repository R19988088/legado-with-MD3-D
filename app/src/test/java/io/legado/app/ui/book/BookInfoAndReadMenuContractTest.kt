package io.legado.app.ui.book

import java.io.File
import org.junit.Assert.assertTrue
import org.junit.Test

class BookInfoAndReadMenuContractTest {

    private val sourceRoot = File("src/main")

    @Test
    fun bookInfoTopBar_exposesGroupManagementAction() {
        val source = File(
            sourceRoot,
            "java/io/legado/app/ui/book/info/BookInfoScreen.kt"
        ).readText()

        assertTrue(source.contains("Icons.Default.Bookmarks"))
        assertTrue(source.contains("onGroupClick = { onIntent(BookInfoIntent.GroupClick) }"))
        assertTrue(source.split("onGroupClick = onGroupClick").size - 1 >= 2)
    }

    @Test
    fun readerMenu_brightnessBlankArea_doesNotRemainATouchTarget() {
        val layouts = listOf(
            File(sourceRoot, "res/layout/view_read_menu.xml"),
            File(sourceRoot, "res/layout-land/view_read_menu.xml"),
        )

        layouts.forEach { layout ->
            val source = layout.readText()
            val brightness = source.between(
                "android:id=\"@+id/ll_brightness\"",
                "android:id=\"@+id/bottom_menu\"",
            )
            val wrapper = brightness.between(
                "VerticalSeekBarWrapper",
                "/VerticalSeekBarWrapper",
            )
            assertTrue(brightness.contains("android:clickable=\"false\""))
            assertTrue(brightness.contains("android:focusable=\"false\""))
            assertTrue(wrapper.contains("android:clickable=\"false\""))
            assertTrue(wrapper.contains("@+id/seek_brightness"))
        }
    }

    @Test
    fun readerDownloadButton_startsWholeBookWithoutRangeDialog() {
        val source = File(
            sourceRoot,
            "java/io/legado/app/ui/book/read/BaseReadBookActivity.kt"
        ).readText()
        val method = source.between("fun startDownloadAll()", "fun showSimulatedReading()")

        assertTrue(method.contains("CacheBook.start(this@BaseReadBookActivity, book, 0, book.totalChapterNum - 1)"))
        assertTrue(method.contains("CacheBook.cacheBookMap[book.bookUrl]?.hasQueuedDownloads() == true"))
        assertTrue(method.contains("CacheBook.pendingAdmissionFlow.value.containsKey(book.bookUrl)"))
        assertTrue(method.contains("downloadAllAdmissions.add(book.bookUrl)"))
        assertTrue(method.contains("downloadAllAdmissions.remove(book.bookUrl)"))
        assertTrue(!method.contains("DialogDownloadChoiceBinding"))
    }

    private fun String.between(start: String, end: String): String {
        assertTrue("missing start marker: $start", contains(start))
        val afterStart = substringAfter(start)
        assertTrue("missing end marker: $end", afterStart.contains(end))
        return afterStart.substringBefore(end)
    }
}
