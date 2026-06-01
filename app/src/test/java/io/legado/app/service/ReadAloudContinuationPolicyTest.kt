package io.legado.app.service

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ReadAloudContinuationPolicyTest {

    @Test
    fun systemPauseDoesNotBlockChapterAutoResume() {
        val policy = ReadAloudContinuationPolicy()

        policy.onPlayStarted()
        policy.onSystemPause()

        assertTrue(policy.shouldAutoResumeAfterChapterChange())
    }

    @Test
    fun manualPauseBlocksChapterAutoResumeUntilResume() {
        val policy = ReadAloudContinuationPolicy()

        policy.onPlayStarted()
        policy.onManualPause()
        assertFalse(policy.shouldAutoResumeAfterChapterChange())

        policy.onResume()

        assertTrue(policy.shouldAutoResumeAfterChapterChange())
    }
}
