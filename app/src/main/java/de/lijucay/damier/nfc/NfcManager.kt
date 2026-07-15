package de.lijucay.damier.nfc

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.nfc.NfcAdapter
import android.util.Log
import de.lijucay.damier.R
import de.lijucay.damier.core.domain.ActivityRepository
import de.lijucay.damier.core.presentation.SnackbarEvent
import de.lijucay.damier.core.presentation.viewmodels.UIViewModel
import de.lijucay.damier.nfc.read.CueReadManager
import de.lijucay.damier.nfc.read.ReadResult
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
        nfcAdapter?.enableReaderMode(
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
        nfcAdapter?.disableReaderMode(activity)
    }

    suspend fun handleNfcIntent(intent: Intent) {
        Log.e("Damier-NFC", "${intent.action}")

        if (intent.action != NfcAdapter.ACTION_NDEF_DISCOVERED) return

        val result = cueReadManager.read(intent)

        Log.e("Damier-NFC", "$result")

        if (result !is ReadResult.Success) return
        if (result.host != context.getString(R.string.host)) return

        val wasJustWritten = recentNfcWriteTracker.wasJustWritten(result.chipId)

        Log.e("Damier-NFC", "$wasJustWritten")

        if (wasJustWritten) return

        val chipId = runCatching {
            Log.e("Damier-NFC", result.chipId)
            UUID.fromString(result.chipId)
        }.getOrNull() ?: return

        val checkInResult = activityRepository.checkInByNfcChipId(chipId)

        checkInResult?.let {
            Log.e("Damier-NFC", "$it")
        } ?: return

        widgetState.updateWidgetForActivity(checkInResult.activityId)

        uiViewModel.emitSnackbar(
            SnackbarEvent(context.getString(R.string.check_in_done, checkInResult.activityName))
        )
    }
}