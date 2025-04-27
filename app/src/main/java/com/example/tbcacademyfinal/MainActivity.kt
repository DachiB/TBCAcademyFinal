package com.example.tbcacademyfinal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.tbcacademyfinal.domain.repository.DataStoreRepository
import com.example.tbcacademyfinal.presentation.navigation.AppNavGraph
import com.example.tbcacademyfinal.presentation.theme.TBCAcademyFinalTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var dataStoreRepo: DataStoreRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val darkTheme by dataStoreRepo.getAppTheme()
                .collectAsState(initial = isSystemInDarkTheme())

            TBCAcademyFinalTheme(darkTheme = darkTheme) {
                val navController = rememberNavController()
                AppNavGraph(navController)
            }
        }
    }

}


@Preview(showBackground = true)
@Composable
fun Preview() {
    AppNavGraph(navController = rememberNavController())
}

