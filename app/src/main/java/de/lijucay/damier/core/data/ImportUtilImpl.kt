package de.lijucay.damier.core.data

import android.content.Context
import android.net.Uri
import androidx.room.withTransaction
import com.google.firebase.Firebase
import com.google.firebase.crashlytics.crashlytics
import com.google.firebase.crashlytics.recordException
import com.google.gson.stream.JsonReader
import de.lijucay.damier.core.data.daos.ActivityInfoDao
import de.lijucay.damier.core.data.daos.CheckInDao
import de.lijucay.damier.core.data.daos.StreakDao
import de.lijucay.damier.core.data.entities.ActivityInfo
import de.lijucay.damier.core.data.entities.CheckInInfo
import de.lijucay.damier.core.data.entities.Streak
import de.lijucay.damier.core.domain.ImportUtil
import de.lijucay.damier.shared.CustomGson
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
        onComplete: (Boolean) -> Unit
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
                1,2,3 -> importDataFromV1(fileUri, onTotalCountUpdate, onCurrentCountUpdate, onComplete)
                4 -> importDataFromV4(fileUri, onTotalCountUpdate, onComplete)
                else -> onComplete(false)
            }
        }
    }

    private suspend fun importDataFromV1(
        fileUri: Uri,
        onTotalCountUpdate: (Int) -> Unit,
        onCurrentCountUpdate: (Int) -> Unit,
        onComplete: (Boolean) -> Unit
    ) {
        var currentCount = 0

        withContext(Dispatchers.IO) {
            try {
                calculateCount(fileUri, onTotalCountUpdate)

                contentResolver.openInputStream(fileUri)?.use { inputStream ->
                    val gson = CustomGson.buildGson()

                    val jsonReader = JsonReader(InputStreamReader(inputStream))
                    jsonReader.beginObject()

                    while (jsonReader.hasNext()) {
                        when (jsonReader.nextName()) {
                            BackupConstants.ACTIVITIES_KEY -> {
                                jsonReader.beginArray()
                                while (jsonReader.hasNext()) {
                                    val activity = gson.fromJson<ActivityInfo>(jsonReader, ActivityInfo::class.java)
                                    activityInfoDao.upsert(activity)
                                    currentCount++
                                    onCurrentCountUpdate(currentCount)
                                }
                                jsonReader.endArray()
                            }

                            BackupConstants.CHECK_INS_KEY -> {
                                jsonReader.beginArray()
                                while (jsonReader.hasNext()) {
                                    val checkIn = gson.fromJson<CheckInInfo>(
                                        jsonReader,
                                        CheckInInfo::class.java
                                    )

                                    checkInDao.upsert(checkIn)
                                    currentCount++
                                    onCurrentCountUpdate(currentCount)
                                }
                                jsonReader.endArray()
                            }

                            BackupConstants.STREAKS_KEY -> {
                                jsonReader.beginArray()
                                while (jsonReader.hasNext()) {
                                    val streak = gson.fromJson<Streak>(
                                        jsonReader,
                                        Streak::class.java
                                    )

                                    streakDao.upsert(streak)
                                    currentCount++
                                    onCurrentCountUpdate(currentCount)
                                }
                                jsonReader.endArray()
                            }

                            else -> jsonReader.skipValue()
                        }
                    }

                    jsonReader.endObject()
                    onComplete(true)
                }
            } catch (e: Exception) {
                Firebase.crashlytics.recordException(e) {
                    key("file_uri", fileUri.toString())
                    key("current_count", currentCount)
                }
                onComplete(false)
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
            } catch (_: Exception) {
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