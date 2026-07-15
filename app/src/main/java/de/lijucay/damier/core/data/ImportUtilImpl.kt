package de.lijucay.damier.core.data

import android.content.Context
import android.net.Uri
import androidx.room.withTransaction
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.stream.JsonReader
import de.lijucay.damier.core.data.converter.UUIDSerializer
import de.lijucay.damier.core.data.daos.ActivityInfoDao
import de.lijucay.damier.core.data.daos.CheckInDao
import de.lijucay.damier.core.data.daos.NfcChipDao
import de.lijucay.damier.core.data.daos.StreakDao
import de.lijucay.damier.core.data.entities.ActivityInfo
import de.lijucay.damier.core.data.entities.CheckInInfo
import de.lijucay.damier.core.data.entities.NfcChipInfo
import de.lijucay.damier.core.data.entities.Streak
import de.lijucay.damier.core.domain.ImportUtil
import de.lijucay.damier.shared.ReferenceType
import de.lijucay.damier.shared.UnitId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonObject
import java.io.InputStreamReader
import java.time.LocalDateTime
import kotlinx.serialization.Serializable
import java.util.UUID

class ImportUtilImpl(
    private val context: Context,
    private val database: DamierDatabase,
    private val activityInfoDao: ActivityInfoDao,
    private val checkInDao: CheckInDao,
    private val streakDao: StreakDao,
    private val nfcChipDao: NfcChipDao
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
                4 -> importDataFromV4(fileUri, onComplete)
                5 -> importDataFromV5(fileUri, onComplete)
                else -> onComplete(false)
            }
        }
    }

    private suspend fun importDataFromV4(
        fileUri: Uri,
        onComplete: (Boolean) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            try {
                contentResolver.openInputStream(fileUri)?.use { inputStream ->
                    val root = json.parseToJsonElement(inputStream.reader().readText()).jsonObject

                    val legacyActivities = json.decodeFromJsonElement<List<ActivityInfoV4>>(
                        root[BackupConstants.ACTIVITIES_KEY] ?: JsonArray(emptyList())
                    )
                    val checkIns = json.decodeFromJsonElement<List<CheckInInfo>>(
                        root[BackupConstants.CHECK_INS_KEY] ?: JsonArray(emptyList())
                    )
                    val streaks = json.decodeFromJsonElement<List<Streak>>(
                        root[BackupConstants.STREAKS_KEY] ?: JsonArray(emptyList())
                    )
                    val activities = legacyActivities.map { it.toActivityInfo() }
                    val backfilledChips = legacyActivities.mapNotNull { activity ->
                        activity.nfcChipId?.let {
                            NfcChipInfo(
                                chipId = activity.id,
                                activityId = activity.id,
                                linkedAt = LocalDateTime.now()
                            )
                        }
                    }

                    database.withTransaction {
                        activityInfoDao.upsertAll(activities)
                        checkInDao.upsertAll(checkIns)
                        streakDao.upsertAll(streaks)
                        nfcChipDao.upsertAll(backfilledChips)
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

    private suspend fun importDataFromV5(
        fileUri: Uri,
        onComplete: (Boolean) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            try {
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

                    val nfcChips = json.decodeFromJsonElement<List<NfcChipInfo>>(
                        root[BackupConstants.NFC_CHIPS_KEY] ?: JsonArray(emptyList())
                    )

                    database.withTransaction {
                        activityInfoDao.upsertAll(activities)
                        checkInDao.upsertAll(checkIns)
                        streakDao.upsertAll(streaks)
                        nfcChipDao.upsertAll(nfcChips)
                    }

                    onComplete(true)
                }
            } catch (e: Exception) {
                FirebaseCrashlytics.getInstance().apply {
                    setCustomKey("import_version", 5)
                    recordException(e)
                }
                onComplete(false)
            }
        }
    }
}

@Serializable
private data class ActivityInfoV4(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val activityName: String,
    val unit: UnitId,
    val reference: Int,
    val referenceType: ReferenceType,
    val defaultAmount: Int,
    val nfcChipId: String? = null
) {
    fun toActivityInfo() = ActivityInfo(
        id = id,
        activityName = activityName,
        unit = unit,
        reference = reference,
        referenceType = referenceType,
        defaultAmount = defaultAmount
    )
}