package de.lijucay.damier.core.data

import android.content.Context
import android.net.Uri
import androidx.room.withTransaction
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.stream.JsonReader
import de.lijucay.damier.core.data.daos.ActivityInfoDao
import de.lijucay.damier.core.data.daos.CheckInDao
import de.lijucay.damier.core.data.daos.StreakDao
import de.lijucay.damier.core.data.entities.ActivityInfo
import de.lijucay.damier.core.data.entities.CheckInInfo
import de.lijucay.damier.core.data.entities.Streak
import de.lijucay.damier.core.domain.ImportUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonObject
import java.io.InputStreamReader

class ImportUtilImpl(
    private val context: Context,
    private val database: DamierDatabase,
    private val activityInfoDao: ActivityInfoDao,
    private val checkInDao: CheckInDao,
    private val streakDao: StreakDao
) : ImportUtil {
    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    private val contentResolver by lazy { context.contentResolver }

    override suspend fun importData(
        fileUri: Uri,
        onTotalCountUpdate: (Int) -> Unit,
        onCurrentCountUpdate: (Int) -> Unit,
        onComplete: (Boolean) -> Unit,
        onIncompatibleVersion: () -> Unit
    ) {
        withContext(Dispatchers.IO) {
            val contentResolver = context.contentResolver
            var fileVersion = 0

            contentResolver.openInputStream(fileUri)?.use { inputStream ->
                val reader = JsonReader(InputStreamReader(inputStream))
                reader.beginObject()

                while (reader.hasNext()) {
                    when (reader.nextName()) {
                        BackupConstants.VERSION_KEY -> fileVersion = reader.nextInt()
                        else -> reader.skipValue()
                    }
                }

                reader.endObject()
            }

            when (fileVersion) {
                1,2,3 -> onIncompatibleVersion()
                4 -> importDataFromV4(fileUri, onTotalCountUpdate, onComplete)
                else -> onComplete(false)
            }
        }
    }

    private suspend fun importDataFromV4(
        fileUri: Uri,
        onTotalCountUpdate: (Int) -> Unit,
        onComplete: (Boolean) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            try {
                calculateCount(fileUri, onTotalCountUpdate)

                contentResolver.openInputStream(fileUri)?.use { inputStream ->
                    val root = json.parseToJsonElement(inputStream.reader().readText()).jsonObject

                    val activities = json.decodeFromJsonElement<List<ActivityInfo>>(
                        root[BackupConstants.ACTIVITIES_KEY] ?: JsonArray(emptyList())
                    )
                    val checkIns = json.decodeFromJsonElement<List<CheckInInfo>>(
                        root[BackupConstants.CHECK_INS_KEY] ?: JsonArray(emptyList())
                    )
                    val streaks = json.decodeFromJsonElement<List<Streak>>(
                        root[BackupConstants.STREAKS_KEY] ?: JsonArray(emptyList())
                    )

                    database.withTransaction {
                        activityInfoDao.upsertAll(activities)
                        checkInDao.upsertAll(checkIns)
                        streakDao.upsertAll(streaks)
                    }

                    onComplete(true)
                }
            } catch (e: Exception) {
                FirebaseCrashlytics.getInstance().apply {
                    setCustomKey("import_version", 4)
                    recordException(e)
                }
                onComplete(false)
            }
        }
    }

    override suspend fun calculateCount(fileUri: Uri, onTotalCountUpdate: (Int) -> Unit) {
        withContext(Dispatchers.IO) {
            var totalCount = 0

            contentResolver.openInputStream(fileUri)?.use { inputStream ->
                val jsonReader = JsonReader(InputStreamReader(inputStream))
                jsonReader.beginObject()

                while (jsonReader.hasNext()) {
                    when (jsonReader.nextName()) {
                        BackupConstants.ACTIVITIES_KEY,
                        BackupConstants.CHECK_INS_KEY,
                        BackupConstants.STREAKS_KEY -> {
                            jsonReader.beginArray()
                            while(jsonReader.hasNext()) {
                                jsonReader.skipValue()
                                totalCount++
                            }
                            jsonReader.endArray()
                        }

                        else -> jsonReader.skipValue()
                    }
                }

                jsonReader.endObject()
                onTotalCountUpdate(totalCount)
            }
        }
    }
}