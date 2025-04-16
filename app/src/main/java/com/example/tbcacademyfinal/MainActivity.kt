package com.example.tbcacademyfinal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.tbcacademyfinal.presentation.navigation.AppNavGraph
import com.example.tbcacademyfinal.presentation.ui.theme.TBCAcademyFinalTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TBCAcademyFinalTheme {
                AppNavGraph()
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun Preview() {
    AppNavGraph()
}