package com.example.tbcacademyfinal.presentation.ui.main.profile.components

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.tbcacademyfinal.common.LocaleHelper
import com.example.tbcacademyfinal.presentation.ui.main.profile.ProfileIntent
import com.example.tbcacademyfinal.presentation.ui.main.profile.ProfileState
import kotlinx.coroutines.launch

@Composable
fun LanguageToggleButton(
    modifier: Modifier = Modifier,
    state: ProfileState,
    processIntent: (ProfileIntent) -> Unit
) {
    // 1️⃣ Grab the current Context & Activity
    val context = LocalContext.current
    val activity = (context as? ComponentActivity)
    val scope = rememberCoroutineScope()

    // 2️⃣ Observe the stored language (fallbacks handled by your DataStore repo)

    Button(onClick = {
        scope.launch {
            val newLang = if (state.currentLanguage == "en") "ka" else "en"

            LocaleHelper.setLocale(context, newLang)

            processIntent(ProfileIntent.LanguageChanged(newLang))

            activity?.recreate()
            Log.d("ProfileScreen", "Language changed to $newLang")
        }
    }) {
        Text(
            text = when (state.currentLanguage) {
                "en" -> "Switch to Georgian"
                "ka" -> "Switch to English"
                else -> "Switch Language"
            }
        )
    }
}