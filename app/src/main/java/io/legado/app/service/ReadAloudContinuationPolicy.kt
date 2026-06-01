package io.legado.app.service

internal class ReadAloudContinuationPolicy {

    private var manuallyPaused = false

    fun onPlayStarted() {
        manuallyPaused = false
    }

    fun onResume() {
        manuallyPaused = false
    }

    fun onManualPause() {
        manuallyPaused = true
    }

    fun onSystemPause() {
        // 保留手动暂停状态；系统暂停不应阻断自动翻章续读。
    }

    fun shouldAutoResumeAfterChapterChange(): Boolean {
        return !manuallyPaused
    }
}
