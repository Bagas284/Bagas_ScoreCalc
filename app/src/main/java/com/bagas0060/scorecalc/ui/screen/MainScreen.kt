package com.bagas0060.scorecalc.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bagas0060.scorecalc.R
import com.bagas0060.scorecalc.navigation.Screen
import com.bagas0060.scorecalc.ui.components.MainTopAppBar
import com.bagas0060.scorecalc.ui.theme.ScoreCalcTheme
import com.bagas0060.scorecalc.util.SettingsDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun MainScreen(navController: NavHostController) {
    val dataStore = SettingsDataStore(LocalContext.current)
    val themeDisplay by dataStore.getTheme().collectAsState(initial = false)

    Scaffold(
        topBar = {
            var expanded by remember { mutableStateOf(false)}
            MainTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.app_name),
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = {
                        CoroutineScope(Dispatchers.IO).launch {
                            dataStore.setTheme(!themeDisplay)
                        }
                    }){
                        Icon(
                            painter = painterResource(
                                if (themeDisplay) R.drawable.baseline_light_mode_24
                                else R.drawable.baseline_dark_mode_24
                            ),
                            contentDescription = null,
                            tint = if (themeDisplay) Color.White else Color.Black
                        )
                    }

                    IconButton(onClick = {
                        expanded = true
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.MoreVert,
                            contentDescription = stringResource(R.string.menuOverflow),
                            tint = Color.White
                        )
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false}
                    ) {
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.tentang_aplikasi))},
                            onClick = {
                                navController.navigate(Screen.About.route)
                            }
                        )
                        HorizontalDivider()

                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.tanyaAplikasi))},
                            onClick = {}
                        )

                        HorizontalDivider()

                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.penilaian))},
                            onClick = {}
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        ScreenContent(Modifier.padding(innerPadding), navController)
    }
}

@Composable
fun ScreenContent(modifier: Modifier = Modifier, navController: NavHostController) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.kalkulator),
            contentDescription = stringResource(R.string.deskripsiLogoHome),
            modifier = Modifier.padding(top = 30.dp)
        )
        Text(
            text = stringResource(R.string.judul),
            fontSize = MaterialTheme.typography.displaySmall.fontSize,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = stringResource(R.string.isi),
            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
            lineHeight = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp)
        ) {

            ElevatedButton(
                onClick = { navController.navigate((Screen.DisplayIpSemester.route))},
                modifier = Modifier
                    .height(50.dp)
                    .weight(1f)
                    .padding(start = 4.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(colorResource(R.color.red))
            ) {
                Text(text = stringResource(R.string.b_start), color = Color.White)
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun MainScreenPreview() {
    ScoreCalcTheme {
        MainScreen(rememberNavController())
    }
}