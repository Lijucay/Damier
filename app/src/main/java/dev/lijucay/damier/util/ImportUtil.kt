package dev.lijucay.damier.util

import android.content.Context
import android.net.Uri
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.lijucay.damier.data.local.database.HabitDao
import dev.lijucay.damier.data.local.database.TrackingInfoDao
import dev.lijucay.damier.data.local.model.Habit
import dev.lijucay.damier.data.local.model.TrackingInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import javax.inject.Inject

interface ImportUtil {
    suspend fun importData(
        fileUri: Uri,
        onTotalCountUpdate: (Int) -> Unit,
        onCurrentCountUpdate: (Int) -> Unit,
        onComplete: (Boolean) -> Unit
    )
    suspend fun importDataFromV1(
        fileUri: Uri,
        onTotalCountUpdate: (Int) -> Unit,
        onCurrentCountUpdate: (Int) -> Unit,
        onComplete: (Boolean) -> Unit
    )
    suspend fun calculateCount(fileUri: Uri, onTotalCountUpdate: (Int) -> Unit)
}

class ImportUtilImpl @Inject constructor(
    private val habitDao: HabitDao,
    private val trackingInfoDao: TrackingInfoDao,
    @ApplicationContext private val context: Context
) : ImportUtil {
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
                        "version" -> fileVersion = reader.nextInt()
                        else -> reader.skipValue()
                    }
                }

                reader.endObject()
            }

            when (fileVersion) {
                1 -> importDataFromV1(fileUri, onTotalCountUpdate, onCurrentCountUpdate, onComplete)
                else -> onComplete(false)
            }
    }
    }

    override suspend fun importDataFromV1(
        fileUri: Uri,
        onTotalCountUpdate: (Int) -> Unit,
        onCurrentCountUpdate: (Int) -> Unit,
        onComplete: (Boolean) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            try {
                var currentCount = 0

                calculateCount(fileUri, onTotalCountUpdate)

                contentResolver.openInputStream(fileUri)?.use { inputStream ->
                    val jsonReader = JsonReader(InputStreamReader(inputStream))
                    jsonReader.beginObject()
                    val gson = Gson()

                    while (jsonReader.hasNext()) {
                        when (jsonReader.nextName()) {
                            "habits" -> {
                                jsonReader.beginArray()
                                while (jsonReader.hasNext()) {
                                    val habit = gson.fromJson<Habit>(jsonReader, Habit::class.java)
                                    habitDao.insertHabit(habit)
                                    currentCount++
                                    onCurrentCountUpdate(currentCount)
                                }
                                jsonReader.endArray()
                            }
                            "trackingInfo" -> {
                                jsonReader.beginArray()
                                while (jsonReader.hasNext()) {
                                    val trackingInfo = gson.fromJson<TrackingInfo>(jsonReader, TrackingInfo::class.java)
                                    trackingInfoDao.insertTrackingInfo(trackingInfo)
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
                e.printStackTrace()
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
                        "habits" -> {
                            jsonReader.beginArray()
                            while (jsonReader.hasNext()) {
                                jsonReader.skipValue()
                                totalCount++
                            }
                            jsonReader.endArray()
                        }
                        "trackingInfo" -> {
                            jsonReader.beginArray()
                            while (jsonReader.hasNext()) {
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