package de.lijucay.damier.nfc

class RecentNfcWriteTracker {
    private var lastWrittenChipId: String? = null
    private var lastWriteTimeTracker: Long = 0L

    fun markWritten(chipId: String) {
        lastWrittenChipId = chipId
        lastWriteTimeTracker = System.currentTimeMillis()
    }

    fun wasJustWritten(chipId: String, cooldownMillis: Long = 2000L): Boolean {
        return chipId == lastWrittenChipId &&
                (System.currentTimeMillis() - lastWriteTimeTracker) < cooldownMillis
    }
}