package com.example.tbcacademyfinal.presentation.ui.main.profile.settings

import androidx.activity.ComponentActivity
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.tbcacademyfinal.common.LocaleHelper
import kotlinx.coroutines.launch

@Composable
fun LanguageToggleButton(
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    // 1️⃣ Grab the current Context & Activity
    val context = LocalContext.current
    val activity = (context as? ComponentActivity)
    val scope = rememberCoroutineScope()

    // 2️⃣ Observe the stored language (fallbacks handled by your DataStore repo)
    val currentLanguage by settingsViewModel.languageFlow.collectAsStateWithLifecycle()

    Button(onClick = {
        scope.launch {
            // 3️⃣ Compute the new language code
            val newLang = if (currentLanguage == "en") "ka" else "en"

            // 4️⃣ Update Android’s resources via your helper
            LocaleHelper.setLocale(context, newLang)

            // 5️⃣ Persist the new choice
            settingsViewModel.setLanguage(newLang)

            // 6️⃣ Recreate the Activity so XML strings & layouts reload
            activity?.recreate()
        }
    }) {
        Text(
            text = when (currentLanguage) {
                "en" -> "Switch to Georgian"
                "ka" -> "Switch to English"
                else -> "Switch Language"
            }
        )
    }
}