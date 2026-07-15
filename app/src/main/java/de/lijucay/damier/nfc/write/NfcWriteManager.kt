package de.lijucay.damier.nfc.write

import android.nfc.FormatException
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.Tag
import android.nfc.TagLostException
import android.nfc.tech.Ndef
import android.nfc.tech.NdefFormatable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.IOException
import java.util.UUID

class NfcWriteManager {
    companion object {
        private const val CUE_SCHEME = "cue://"
    }

    suspend fun write(tag: Tag, host: String): WriteResult =
        write(tag, host, UUID.randomUUID())

    suspend fun write(tag: Tag, host: String, id: UUID): WriteResult = withContext(Dispatchers.IO) {
        val chipId = id.toString()
        val message = createNdefMessage(chipId, host)

        val ndef = Ndef.get(tag)
        if (ndef != null) return@withContext writeToNdef(ndef, message, chipId)

        val formattable = NdefFormatable.get(tag)
        if (formattable != null) return@withContext formatAndWrite(formattable, message, chipId)

        WriteResult.NotNdefCompatible
    }

    suspend fun erase(tag: Tag): WriteResult = withContext(Dispatchers.IO) {
        val ndef = Ndef.get(tag) ?: return@withContext WriteResult.NotErasable

        try {
            ndef.connect()

            val emptyRecord = NdefRecord(
                NdefRecord.TNF_EMPTY,
                ByteArray(0),
                ByteArray(0),
                ByteArray(0)
            )

            ndef.writeNdefMessage(NdefMessage(arrayOf(emptyRecord)))
            WriteResult.EraseSuccess
        } catch (e: FormatException) {
            WriteResult.EraseException(e)
        } catch (e: IOException) {
            WriteResult.UnknownError(e)
        } finally {
            runCatching { ndef.close() }
        }
    }

    private fun writeToNdef(ndef: Ndef, message: NdefMessage, chipId: String): WriteResult {
        return try {
            ndef.connect()

            if (!ndef.isWritable) return WriteResult.NotWriteable
            if (ndef.maxSize < message.toByteArray().size) return WriteResult.InsufficientSize

            ndef.writeNdefMessage(message)
            WriteResult.Success(chipId)
        } catch (_: TagLostException) {
            WriteResult.TagLost
        } catch (e: IOException) {
            WriteResult.UnknownError(e)
        } catch (e: FormatException) {
            WriteResult.UnknownError(e)
        } finally {
            runCatching { ndef.close() }
        }
    }

    private fun formatAndWrite(formattable: NdefFormatable, message: NdefMessage, chipId: String): WriteResult {
        return try {
            formattable.connect()
            formattable.format(message)
            WriteResult.Success(chipId)
        } catch (_: TagLostException) {
            WriteResult.TagLost
        } catch (e: IOException) {
            WriteResult.UnknownError(e)
        } catch (e: FormatException) {
            WriteResult.UnknownError(e)
        } finally {
            runCatching { formattable.close() }
        }
    }

    private fun createNdefMessage(chipId: String, host: String): NdefMessage {
        val uri = "$CUE_SCHEME$host/$chipId"
        val record = NdefRecord.createUri(uri)

        return NdefMessage(arrayOf(record))
    }
}