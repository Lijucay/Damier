package de.lijucay.damier.nfc

import android.content.Context
import android.content.Intent
import android.nfc.NfcAdapter
import android.util.Log
import de.lijucay.cue_read.CueReadManager
import de.lijucay.cue_read.ReadResult
import de.lijucay.damier.R
import de.lijucay.damier.activity_list.presentation.ActivityListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NfcManager(
    private val listViewModel: ActivityListViewModel,
    private val context: Context
) {
    suspend fun handleNfcIntent(intent: Intent) {
        Log.d("CUE", "handleNfcIntent")
        if (intent.action == NfcAdapter.ACTION_NDEF_DISCOVERED) {
            handleNfcRead(intent)
        }
    }

    private suspend fun handleNfcRead(intent: Intent) {
        Log.d("CUE", "handleNfcRead")

        val result = withContext(Dispatchers.IO) {
            CueReadManager().read(intent)
        }

        if (result !is ReadResult.Success) return
        if (result.host != context.getString(R.string.host)) return
        listViewModel.checkInByNfcChipId(result.chipId)
    }
}