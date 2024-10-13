package dev.lijucay.damier.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

object DataStore {
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")
    val SHOW_UNIT_INFO_CARD = booleanPreferencesKey("show_unit_info_card")
    // --Commented out by Inspection (08.10.2024 11:32):val DARK_THEME = intPreferencesKey("dark_theme")
    val SHOW_GOAL = booleanPreferencesKey("show_goal")
    val DEFAULT_GOAL = intPreferencesKey("default_goal")
}