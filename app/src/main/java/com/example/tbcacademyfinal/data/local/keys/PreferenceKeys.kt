package com.example.tbcacademyfinal.data.local.keys

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.byteArrayPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.tbcacademyfinal.presentation.misc.ThemeType

object PreferencesKeys {
    val SEEN_LANDING = booleanPreferencesKey("has_seen_landing")
    val SHOULD_REMEMBER_USER = booleanPreferencesKey("remember_user")
    val DARK_THEME_KEY = booleanPreferencesKey("dark_theme")
    val LANGUAGE_KEY = stringPreferencesKey("language") // Store enum name as String
}