package com.bagas0060.scorecalc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.bagas0060.scorecalc.navigation.SetupNavGraph
import com.bagas0060.scorecalc.ui.theme.ScoreCalcTheme
import com.bagas0060.scorecalc.util.SettingsDataStore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val dataStore = SettingsDataStore(LocalContext.current)
            val themeDisplay by dataStore.getTheme().collectAsState(initial = false)

            ScoreCalcTheme(isDarkTheme = themeDisplay) {
                SetupNavGraph()
            }
        }
    }
}