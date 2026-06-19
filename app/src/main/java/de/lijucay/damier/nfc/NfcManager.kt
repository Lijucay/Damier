package de.lijucay.damier.nfc

import android.content.Context
import android.content.Intent
import android.nfc.NfcAdapter
import android.widget.Toast
import de.lijucay.cue_read.CueReadManager
import de.lijucay.cue_read.ReadResult
import de.lijucay.damier.R
import de.lijucay.damier.core.domain.ActivityRepository
import de.lijucay.damier.core.presentation.SnackbarEvent
import de.lijucay.damier.core.presentation.viewmodels.UIViewModel
import de.lijucay.damier.widget.presentation.DamierWidgetState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NfcManager(
    private val activityRepository: ActivityRepository,
    private val widgetState: DamierWidgetState,
    private val uiViewModel: UIViewModel,
    private val context: Context,
) {
    suspend fun handleNfcIntent(intent: Intent) {
        if (intent.action == NfcAdapter.ACTION_NDEF_DISCOVERED) {
            handleNfcRead(intent)
        }
    }

    private suspend fun handleNfcRead(intent: Intent) {
        val result = withContext(Dispatchers.IO) {
            CueReadManager().read(intent)
        }

        if (result !is ReadResult.Success) return
        if (result.host != context.getString(R.string.host)) return

        val checkInResult = activityRepository.checkInByNfcChipId(result.chipId) ?: return

        widgetState.updateWidgetForActivity(checkInResult.activityId)

        uiViewModel.emitSnackbar(
            SnackbarEvent(context.getString(R.string.check_in_done, checkInResult.activityName))
        )
    }
}