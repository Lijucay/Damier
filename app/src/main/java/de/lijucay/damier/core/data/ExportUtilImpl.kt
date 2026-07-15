package de.lijucay.damier.core.data

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import com.google.firebase.Firebase
import com.google.firebase.crashlytics.crashlytics
import com.google.firebase.crashlytics.recordException
import de.lijucay.damier.R
import de.lijucay.damier.core.data.daos.ActivityInfoDao
import de.lijucay.damier.core.data.daos.CheckInDao
import de.lijucay.damier.core.data.daos.NfcChipDao
import de.lijucay.damier.core.data.daos.StreakDao
import de.lijucay.damier.core.domain.DataUtil
import de.lijucay.damier.core.domain.ExportResult
import de.lijucay.damier.core.domain.ExportUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.put
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ExportUtilImpl(
    private val context: Context,
    private val activityInfoDao: ActivityInfoDao,
    private val checkInDao: CheckInDao,
    private val streakDao: StreakDao,
    private val nfcChipDao: NfcChipDao
) : ExportUtil {
    private val json = Json { encodeDefaults = false }

    override suspend fun prepareFileData(): Map<String, String> {
        return withContext(Dispatchers.IO) {
            val activities = activityInfoDao.getActivityInfoList()
            val checkIns = checkInDao.getAllCheckIns()
            val streaks = streakDao.getAllStreaks()
            val nfcChips = nfcChipDao.getAllChips()

            val exportData = buildJsonObject {
                put(BackupConstants.VERSION_KEY, DataUtil.DATABASE_SCHEME_VERSION)
                put(BackupConstants.ACTIVITIES_KEY, json.encodeToJsonElement(activities))
                put(BackupConstants.CHECK_INS_KEY, json.encodeToJsonElement(checkIns))
                put(BackupConstants.STREAKS_KEY, json.encodeToJsonElement(streaks))
                put(BackupConstants.NFC_CHIPS_KEY, json.encodeToJsonElement(nfcChips))
            }

            val exportDataJson = exportData.toString()

            val fileName = "damier_backup_${LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_ss_SSS")
            )}.dmr"
            mapOf(BackupConstants.FILENAME to fileName, BackupConstants.DATA to exportDataJson)
        }
    }

    override suspend fun exportData(uri: Uri): ExportResult {
        return withContext(Dispatchers.IO) {
            val preparedData = prepareFileData()
            val directory = DocumentFile.fromTreeUri(context, uri)

            if (directory == null || !directory.isDirectory) {
                return@withContext ExportResult.Failure(context.getString(R.string.invalid_directory))
            }

            var backupFolder = directory.findFile(BackupConstants.DAMIER_DIR_KEY)
            if (backupFolder == null || !backupFolder.isDirectory)
                backupFolder = directory.createDirectory(BackupConstants.DAMIER_DIR_KEY)

            val backupFile = backupFolder?.createFile(
                BackupConstants.FILE_TYPE,
                preparedData[BackupConstants.FILENAME]
                    ?: BackupConstants.FALLBACK_NAME
            )
            if (backupFile == null)
                return@withContext ExportResult.Failure(context.getString(R.string.failed_to_create_backup))

            return@withContext try {
                preparedData[BackupConstants.DATA]?.let { backupData ->
                    context.contentResolver.openOutputStream(backupFile.uri)?.buffered()?.use { os ->
                        os.write(backupData.toByteArray())
                    }
                }

                ExportResult.Success
            } catch (e: Exception) {
                Firebase.crashlytics.recordException(e) {
                    key("backup_uri", backupFile.uri.toString())
                    key("data_size_byte", preparedData[BackupConstants.DATA]?.length ?: -1)
                }
                ExportResult.Failure(context.getString(R.string.failed_to_create_backup))
            }
        }
    }
}