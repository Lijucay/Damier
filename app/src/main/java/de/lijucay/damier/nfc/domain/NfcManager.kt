package de.lijucay.damier.nfc.domain

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.nfc.NfcAdapter
import de.lijucay.damier.R
import de.lijucay.damier.core.domain.Logger
import de.lijucay.damier.activity.domain.ActivityRepository
import de.lijucay.damier.core.presentation.SnackbarEvent
import de.lijucay.damier.core.presentation.viewmodels.UIViewModel
import de.lijucay.damier.nfc.domain.read.CueReadManager
import de.lijucay.damier.nfc.domain.read.ReadResult
import de.lijucay.damier.widget.presentation.DamierWidgetState
import java.util.UUID

class NfcManager(
    private val activityRepository: ActivityRepository,
    private val widgetState: DamierWidgetState,
    private val uiViewModel: UIViewModel,
    private val context: Context,
    private val recentNfcWriteTracker: RecentNfcWriteTracker,
    private val cueReadManager: CueReadManager
) {
    private val nfcAdapter = NfcAdapter.getDefaultAdapter(context)

    val hasNfc = nfcAdapter != null

    fun enableReaderMode(activity: Activity, callback: NfcAdapter.ReaderCallback) {
        if (nfcAdapter == null) {
            Logger.w(context, "enableReaderMode has been called without a valid NFC-Adapter")
            return
        }

        nfcAdapter.enableReaderMode(
            activity,
            callback,
            NfcAdapter.FLAG_READER_NFC_A or
            NfcAdapter.FLAG_READER_NFC_B or
            NfcAdapter.FLAG_READER_NFC_F or
            NfcAdapter.FLAG_READER_NFC_V,
            null
        )
    }

    fun disableReaderMode(activity: Activity) {
        if (nfcAdapter == null) {
            Logger.w(context, "disableReaderMode has been called without a valid NFC-Adapter")
            return
        }
        nfcAdapter.disableReaderMode(activity)
    }

    suspend fun handleNfcIntent(intent: Intent) {
        val result = cueReadManager.read(intent)

        Logger.i(context, "Read intent with result: $result")

        if (result !is ReadResult.Success) return
        if (result.host != context.getString(R.string.host)) return

        val wasJustWritten = recentNfcWriteTracker.wasJustWritten(result.chipId)

        if (wasJustWritten) {
            Logger.i(context, "NFC tag was just written, ignore reading")
            return
        }

        val chipId = runCatching { UUID.fromString(result.chipId) }.getOrNull()

        if (chipId == null) {
            Logger.w(context, "ChipId was not a valid UUID format, chip is ignored")
            return
        }

        val checkInResult = activityRepository.checkInByNfcChipId(chipId)
        if (checkInResult == null) {
            Logger.w(context, "Couldn't find activity linked to NFC chip, chip is ignored")
            return
        }

        widgetState.updateWidgetForActivity(checkInResult.activityId)

        uiViewModel.emitSnackbar(
            SnackbarEvent(context.getString(R.string.check_in_done, checkInResult.activityName))
        )
    }
}