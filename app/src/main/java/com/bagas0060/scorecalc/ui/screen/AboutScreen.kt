package com.bagas0060.scorecalc.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bagas0060.scorecalc.R
import com.bagas0060.scorecalc.ui.components.MainTopAppBar
import com.bagas0060.scorecalc.ui.theme.ScoreCalcTheme

@Composable
fun AboutScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            MainTopAppBar(
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.b_kembali),
                            tint = Color.White
                        )
                    }
                },
                title = {
                    Text(
                        text = stringResource(R.string.tentang_aplikasi),
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }
    ) { innerPadding ->
        AboutContent(Modifier.padding(innerPadding))
    }
}

@Composable
fun AboutContent(modifier: Modifier = Modifier) {
    val daftarPertanyaan = listOf(
        stringResource(R.string.pertanyaan1) to stringResource(R.string.jawaban1),
        stringResource(R.string.pertanyaan2) to stringResource(R.string.jawaban2),
        stringResource(R.string.pertanyaan3) to stringResource(R.string.jawaban3),
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Image(
            painter = painterResource(R.drawable.about),
            contentDescription = stringResource(R.string.deskripsiLogoAbout),
        )

        daftarPertanyaan.forEach { (question, answer) ->
            Cards (question, answer)
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = stringResource(R.string.copyright),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            modifier = Modifier
                .padding(top = 30.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun Cards(title: String, description: String){
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 15.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .clickable { expanded = !expanded }
                .padding(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    modifier = Modifier
                        .weight(1f)
                )

                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = null
                    )
                }
            }
            if (expanded) {
                Text(
                    text = description,
                    color = Color.Gray,
                    textAlign = TextAlign.Justify,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun AboutScreenPreview() {
    ScoreCalcTheme {
        AboutScreen(rememberNavController())
    }
}
