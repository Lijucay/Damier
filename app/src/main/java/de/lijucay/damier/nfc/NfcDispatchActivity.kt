package de.lijucay.damier.nfc

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import de.lijucay.damier.MainActivity

class NfcDispatchActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val nfcIntent = intent

        startActivity(
            Intent(this, MainActivity::class.java).apply {
                action = nfcIntent.action
                data = nfcIntent.data
                putExtras(nfcIntent)
                addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }
        )

        finish()
    }
}