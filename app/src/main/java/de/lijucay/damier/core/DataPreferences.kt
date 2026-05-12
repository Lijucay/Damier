package de.lijucay.damier.core

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

object DataPreferences {
    val Context.dataStore by preferencesDataStore("settings")

    object Keys {
        val showReference = booleanPreferencesKey("show_reference")
        val showMaxAmount = booleanPreferencesKey("show_max_amount")
        val backupDirUri = stringPreferencesKey("backup_dir_uri")

        val firstLaunch = booleanPreferencesKey("first_launch")

        val activityId = stringPreferencesKey("selected_activity_id")
        val activityName = stringPreferencesKey("selected_activity_name")
    }
}