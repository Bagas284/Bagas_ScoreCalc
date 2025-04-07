package com.bagas0060.scorecalc.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bagas0060.scorecalc.R
import com.bagas0060.scorecalc.model.KomponenPenilaian
import com.bagas0060.scorecalc.navigation.Screen
import com.bagas0060.scorecalc.ui.components.MainTopAppBar
import com.bagas0060.scorecalc.ui.theme.ScoreCalcTheme

@Composable
fun HitungMatkulScreen(navController: NavHostController) {
    val komponenList = remember { mutableStateListOf(KomponenPenilaian()) }

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
                        text = stringResource(R.string.hitung_matkul),
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = {
                        navController.navigate(Screen.About.route)
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = stringResource(R.string.tentang_aplikasi),
                            tint = Color.White
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    komponenList.add(KomponenPenilaian())
                },
                containerColor = colorResource(R.color.red),
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(R.string.buttonTambahKomponen),
                    tint = Color.White
                )
            }
        }
    ) { innerPadding ->
        HitungMatkulContent(
            modifier = Modifier.padding(innerPadding),
            komponenList = komponenList,
            onUpdateKomponen = { index, newValue ->
                komponenList[index] = newValue
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HitungMatkulContent(
    modifier: Modifier = Modifier,
    komponenList: List<KomponenPenilaian>,
    onUpdateKomponen: (Int, KomponenPenilaian) -> Unit
) {
    var namaPengguna by remember { mutableStateOf("") }
    var mataKuliah by remember { mutableStateOf("") }
    var programStudi by remember { mutableStateOf("") }

    var rerata by remember { mutableFloatStateOf(0f) }
    var kategori by remember { mutableStateOf("") }

    val options = listOf(
        "Semester 1",
        "Semester 2",
        "Semester 3",
        "Semester 4",
        "Semester 5",
        "Semester 6",
        "Semester 7",
        "Semester 8"
    )
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf("Semester") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 84.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = stringResource(R.string.labelDataDiri),
            style = MaterialTheme.typography.titleLarge ,
            fontWeight = FontWeight.SemiBold
        )

        // Input nama pengguna
        OutlinedTextField(
            value = namaPengguna,
            onValueChange = { namaPengguna = it },
            label = { Text(stringResource(R.string.labelNamaPengguna)) },
            // isError
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        // Dropdown Prodi
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                readOnly = true,
                value = selectedOptionText,
                onValueChange = {},
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            selectedOptionText = option
                            expanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                }
            }
        }

        // Input program studi
        OutlinedTextField(
            value = programStudi,
            onValueChange = { programStudi = it },
            label = { Text(stringResource(R.string.labelProgramStudi)) },
            // isError
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        // Input mata kuliah
        OutlinedTextField(
            value = mataKuliah,
            onValueChange = { mataKuliah = it },
            label = { Text(stringResource(R.string.labelMataKuliah)) },
            // isError
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        HorizontalDivider()

        Text(
            text = stringResource(R.string.labelKomponenPenilaian),
            style = MaterialTheme.typography.titleLarge ,
            fontWeight = FontWeight.SemiBold
        )

        komponenList.forEachIndexed { index, komponen ->
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = komponen.nama,
                    onValueChange = {
                        onUpdateKomponen(index, komponen.copy(nama = it))
                    },
                    label = { Text(stringResource(R.string.labelKomponenPenilaian)) },
                    // isError
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = komponen.nilai,
                        onValueChange = {
                            onUpdateKomponen(index, komponen.copy(nilai = it))
                        },
                        label = { Text(stringResource(R.string.labelNilai)) },
                        // isError
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = komponen.bobot,
                        onValueChange = {
                            onUpdateKomponen(index, komponen.copy(bobot = it))
                        },
                        label = { Text(stringResource(R.string.labelBobot)) },
                        // isError
                        trailingIcon = { Text(text = "%")},
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            HorizontalDivider()
        }

        Button(
            onClick = {
                var total = 0f
                komponenList.forEach { komponen ->
                    val nilai = komponen.nilai.toFloat()
                    val bobot = komponen.bobot.toFloat()
                    total += hitungNilaiMatkul(nilai, bobot)
                }
                rerata = total
                kategori = getKategoriNilai(total)
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(colorResource(R.color.red))
        ) {
            Text(
                stringResource(R.string.b_hitung),
                color = Color.White
            )
        }

        // Tampilan hasil
        if (rerata != 0f){
            Text(
                text = kategori,
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally),
                color = colorResource(R.color.red)
            )
            Text(
                text = stringResource(R.string.totalHitung, rerata),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)
            )
        }
    }
}

private fun hitungNilaiMatkul(nilai: Float, bobot: Float): Float{
    return (nilai * bobot) / 100
}

private fun getKategoriNilai(nilai: Float): String {
    return when {
        nilai > 85 -> "A"
        nilai > 75 -> "AB"
        nilai > 65 -> "B"
        nilai > 60 -> "BC"
        nilai > 50 -> "C"
        nilai > 40 -> "D"
        else -> "E"
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun HitungMatkulScreenPreview() {
    ScoreCalcTheme {
        HitungMatkulScreen(rememberNavController())
    }
}