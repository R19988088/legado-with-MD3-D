package io.legado.app.utils

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.system.measureTimeMillis

class FlowExtensionsTest {

    @Test
    fun `mapConcurrentUnordered emits completed work without waiting for slow previous item`() = runBlocking {
        var result = emptyList<Int>()
        val elapsed = measureTimeMillis {
            result = flowOf(1, 2, 3)
                .mapConcurrentUnordered(concurrency = 3) { value ->
                    if (value == 1) {
                        delay(200)
                    } else {
                        delay(20)
                    }
                    value
                }
                .toList()
        }

        assertEquals(listOf(2, 3, 1), result)
        assertTrue("Expected concurrent execution, elapsed=${elapsed}ms", elapsed < 300)
    }
}
