package com.example.tbcacademyfinal.data.local.keys

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {
    val SEEN_LANDING = booleanPreferencesKey("has_seen_landing")
    val SHOULD_REMEMBER_USER = booleanPreferencesKey("remember_user")
    val DARK_THEME_KEY = booleanPreferencesKey("dark_theme")
    val LANGUAGE_KEY = stringPreferencesKey("language")
}