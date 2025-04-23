package com.example.tbcacademyfinal.data.local

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {
    val HAS_SEEN_LANDING = booleanPreferencesKey("has_seen_landing")
    val APP_THEME = stringPreferencesKey("app_theme") // Store enum name as String
    val APP_LANGUAGE = stringPreferencesKey("app_language") // Store enum name as String
}