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
import de.lijucay.damier.core.data.daos.StreakDao
import de.lijucay.damier.core.domain.DataUtil
import de.lijucay.damier.core.domain.ExportUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ExportUtilImpl(
    private val context: Context,
    private val activityInfoDao: ActivityInfoDao,
    private val checkInDao: CheckInDao,
    private val streakDao: StreakDao
) : ExportUtil {
    override suspend fun prepareFileData(): Map<String, String> {
        return withContext(Dispatchers.IO) {
            val activities = activityInfoDao.getActivityInfoList()
            val checkIns = checkInDao.getAllCheckIns()
            val streaks = streakDao.getAllStreaks()

            val exportData = mapOf(
                DataUtil.VERSION_KEY to DataUtil.DATABASE_SCHEME_VERSION,
                DataUtil.ACTIVITIES_KEY to activities,
                DataUtil.CHECK_INS_KEY to checkIns,
                DataUtil.STREAKS_KEY to streaks
            )

            val exportDataJson = buildGson().toJson(exportData)

            val currentDate = LocalDateTime.now()
            val dateTimeFormatted = currentDate.format(
                DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss_SSS")
            )

            val fileName = "damier_backup_${dateTimeFormatted}.dmr"
            mapOf(DataUtil.FILENAME to fileName, DataUtil.DATA to exportDataJson)
        }
    }

    override suspend fun exportData(uri: Uri): Pair<Boolean, String?> {
        return withContext(Dispatchers.IO) {
            val preparedData = prepareFileData()
            val directory = DocumentFile.fromTreeUri(context, uri)

            if (directory == null || !directory.isDirectory) {
                return@withContext Pair(false, context.getString(R.string.invalid_directory))
            }

            var backupFolder = directory.findFile(DataUtil.DAMIER_DIR_KEY)
            if (backupFolder == null || !backupFolder.isDirectory)
                backupFolder = directory.createDirectory(DataUtil.DAMIER_DIR_KEY)

            val backupFile = backupFolder?.createFile(DataUtil.FILE_TYPE, preparedData[DataUtil.FILENAME] ?: DataUtil.FALLBACK_NAME)
            if (backupFile == null)
                return@withContext Pair(false, context.getString(R.string.failed_to_create_backup))

            return@withContext try {
                preparedData[DataUtil.DATA]?.let { backupData ->
                    context.contentResolver.openOutputStream(backupFile.uri)?.buffered()?.use { os ->
                        os.write(backupData.toByteArray())
                    }
                }

                Pair(true, null)
            } catch (e: Exception) {
                Firebase.crashlytics.recordException(e) {
                    key("backup_uri", backupFile.uri.toString())
                    key("data_size_byte", preparedData[DataUtil.DATA]?.length ?: -1)
                }
                Pair(false, context.getString(R.string.failed_to_create_backup))
            }
        }
    }
}