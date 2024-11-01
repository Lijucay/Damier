package dev.lijucay.damier.util

import android.content.Context
import android.os.Environment
import androidx.compose.ui.platform.LocalContext
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.lijucay.damier.data.local.database.HabitDao
import dev.lijucay.damier.data.local.database.TrackingInfoDao
import dev.lijucay.damier.util.Utils.DATABASE_SCHEME_VERSION
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileWriter
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

interface ExportUtil {
    suspend fun exportData(): Boolean
}

class ExportUtilImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val habitDao: HabitDao,
    private val trackingInfoDao: TrackingInfoDao
) : ExportUtil {
    override suspend fun exportData(): Boolean {
        return withContext(Dispatchers.IO) {
            val habits = habitDao.getCurrentHabits()
            val trackingInfo = trackingInfoDao.getCurrentTrackingInfo()

            val exportData = mapOf(
                "version" to DATABASE_SCHEME_VERSION,
                "habits" to habits,
                "trackingInfo" to trackingInfo
            )

            val json = Gson().toJson(exportData)

            val exportDir = File(
                "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)}",
                "Damier Database Exports"
            )
            if (!exportDir.exists()) {
                exportDir.mkdirs()
            }

            try {
                val currentDate = LocalDate.now()
                val dateFormatted = currentDate.format(DateTimeFormatter.ofPattern("yyyy_MM_dd"))
                val currentTime = LocalTime.now()
                val timeFormatted = currentTime.format(DateTimeFormatter.ofPattern("HH_mm_ss_SSS"))

                val fileName = "damier_database_${dateFormatted}_${timeFormatted}.dmr"

                val file = File(exportDir, fileName)
                FileWriter(file).use { it.write(json) }
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    fun exportSettings() {

    }

    fun exportAll() {

    }
}