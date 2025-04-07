package com.bagas0060.scorecalc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.bagas0060.scorecalc.navigation.SetupNavGraph
import com.bagas0060.scorecalc.ui.theme.ScoreCalcTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ScoreCalcTheme {
                SetupNavGraph()
            }
        }
    }
}