package de.lijucay.damier.nfc.read

import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.Tag
import android.nfc.tech.Ndef
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.IOException

class NfcReadManager {
    companion object {
        private const val SCHEME = "cue"
    }

    suspend fun read(tag: Tag): ReadResult = withContext(Dispatchers.IO) {
        val ndef = Ndef.get(tag) ?: return@withContext ReadResult.NotNdefCompatible

        try {
            ndef.connect()
            val message = ndef.cachedNdefMessage
                ?: ndef.ndefMessage
                ?: return@withContext ReadResult.EmptyTag

            extractCueChipId(message)
        } catch (e: IOException) {
            ReadResult.UnknownError(e)
        } finally {
            runCatching { ndef.close() }
        }
    }

    fun read(ndefMessage: NdefMessage): ReadResult = extractCueChipId(ndefMessage)

    private fun extractCueChipId(message: NdefMessage): ReadResult {
        for (record in message.records) {
            val (host, chipId) = extractFromRecord(record) ?: continue
            return ReadResult.Success(host, chipId)
        }
        return ReadResult.NotACueChip
    }

    private fun extractFromRecord(record: NdefRecord): Pair<String, String>? {
        if (record.tnf != NdefRecord.TNF_WELL_KNOWN) return null
        if (!record.type.contentEquals(NdefRecord.RTD_URI)) return null

        return try {
            val uri = record.toUri() ?: return null
            if (uri.scheme != SCHEME) return null

            val host = uri.host?.takeIf { it.isNotBlank() } ?: return null
            val chipId = uri.pathSegments.firstOrNull()?.takeIf { it.isNotBlank() } ?: return null

            Pair(host, chipId)
        } catch (_: Exception) {
            null
        }
    }
}