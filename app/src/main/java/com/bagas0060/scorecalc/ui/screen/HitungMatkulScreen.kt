package com.bagas0060.scorecalc.ui.screen

import android.content.Context
import android.content.Intent
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
import androidx.compose.material.icons.filled.Warning
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
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
    val komponenList = rememberSaveable(
        saver = listSaver(
            save = { list -> list.map { listOf(it.nama, it.nilai, it.bobot, it.namaError, it.nilaiError, it.bobotError) } },
            restore = { restored ->
                restored.map {
                    KomponenPenilaian(
                        nama = it[0] as String,
                        nilai = it[1] as String,
                        bobot = it[2] as String,
                        namaError = it[3] as Boolean,
                        nilaiError = it[4] as Boolean,
                        bobotError = it[5] as Boolean
                    )
                }.toMutableStateList()
            }
        )
    ) {
        mutableStateListOf(KomponenPenilaian())
    }

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
    var namaPengguna by rememberSaveable { mutableStateOf("") }
    var namaPenggunaError by rememberSaveable { mutableStateOf(false) }

    var programStudi by rememberSaveable { mutableStateOf("") }
    var programStudiError by rememberSaveable { mutableStateOf(false) }

    var mataKuliah by rememberSaveable { mutableStateOf("") }
    var mataKuliahError by rememberSaveable { mutableStateOf(false) }

    var rerata by rememberSaveable { mutableFloatStateOf(0f) }
    var kategori by rememberSaveable { mutableStateOf("") }

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
    var expanded by rememberSaveable { mutableStateOf(false) }
    var selectedOptionText by rememberSaveable { mutableStateOf("") }
    var semesterError by rememberSaveable { mutableStateOf(false) }

    val context = LocalContext.current

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
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )

        // Input nama pengguna
        OutlinedTextField(
            value = namaPengguna,
            onValueChange = { namaPengguna = it },
            label = { Text(stringResource(R.string.labelNamaPengguna)) },
            trailingIcon = { IconPicker(namaPenggunaError) },
            supportingText = { ErrorHint(namaPenggunaError) },
            isError = namaPenggunaError,
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
                label = { Text(stringResource(R.string.labelPilihSemester)) },
                isError = semesterError,
                supportingText = {
                    if (semesterError) {
                        Text(stringResource(R.string.dropdown_invalid))
                    }
                },
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
            trailingIcon = { IconPicker(programStudiError) },
            supportingText = { ErrorHint(programStudiError) },
            isError = programStudiError,
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
            trailingIcon = { IconPicker(mataKuliahError) },
            supportingText = { ErrorHint(mataKuliahError) },
            isError = mataKuliahError,
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
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )

        komponenList.forEachIndexed { index, komponen ->
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = komponen.nama,
                    onValueChange = {
                        val isValid = it.isNotBlank() && it.matches(Regex("^[a-zA-Z0-9\\s]+\$"))
                        onUpdateKomponen(index, komponen.copy(nama = it, namaError  = !isValid))
                    },
                    label = { Text(stringResource(R.string.labelKomponenPenilaian)) },
                    singleLine = true,
                    isError = komponen.namaError,
                    supportingText = {
                        if(komponen.namaError){
                            Text(stringResource(R.string.input_invalid))
                        }
                    },
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
                            val value = it.toFloatOrNull()
                            val isValid = value != null && value in 0f..100f
                            onUpdateKomponen(
                                index, komponen.copy(
                                    nilai = it,
                                    nilaiError = !isValid
                                )
                            )
                        },
                        label = { Text(stringResource(R.string.labelNilai)) },
                        isError = komponen.nilaiError,
                        supportingText = {
                            if (komponen.nilaiError) {
                                Text(stringResource(R.string.nilai_invalid))
                            }
                        },
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
                            val value = it.toFloatOrNull()
                            val isValid = value != null && value in 1f..100f
                            onUpdateKomponen(
                                index, komponen.copy(
                                    bobot = it,
                                    bobotError = !isValid
                                )
                            )
                        },
                        label = { Text(stringResource(R.string.labelBobot)) },
                        isError = komponen.bobotError,
                        supportingText = {
                            if (komponen.bobotError) {
                                Text(stringResource(R.string.bobot_invalid))
                            }
                        },
                        trailingIcon = { Text(text = "%") },
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
                namaPenggunaError =
                    (namaPengguna.isBlank() || !namaPengguna.matches(Regex("^[a-zA-Z\\s]+$")))
                programStudiError =
                    (programStudi.isBlank() || !programStudi.matches(Regex("^[a-zA-Z0-9\\s]+$")))
                mataKuliahError =
                    (mataKuliah.isBlank() || !mataKuliah.matches(Regex("^[a-zA-Z0-9\\s]+$")))
                semesterError = (selectedOptionText == "")

                var total = 0f
                var valid = true
                komponenList.forEachIndexed { index, komponen ->
                    val nilai = komponen.nilai.toFloatOrNull()
                    val bobot = komponen.bobot.toFloatOrNull()
                    val namaValid = komponen.nama.isNotBlank() && komponen.nama.matches(Regex("^[a-zA-Z0-9\\s]+\$"))

                    if (nilai == null || bobot == null || !namaValid) {
                        onUpdateKomponen(
                            index, komponen.copy(
                                nilaiError = nilai == null,
                                bobotError = bobot == null,
                                namaError = !namaValid
                            )
                        )
                        valid = false
                    } else {
                        total += hitungNilaiMatkul(nilai, bobot)
                    }
                }

                if (
                    namaPenggunaError ||
                    programStudiError ||
                    mataKuliahError ||
                    semesterError ||
                    !valid
                ) return@Button

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
        if (rerata != 0f) {
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

            Button(
                onClick = {
                    val dataPenilaian = komponenList.joinToString("\n") { komponen ->
                        "- ${komponen.nama}: Nilai ${komponen.nilai}, Bobot ${komponen.bobot} %"
                    }
                    shareData(
                        context = context,
                        message = context.getString(
                            R.string.bagikan_template,
                            namaPengguna,
                            selectedOptionText,
                            programStudi,
                            mataKuliah,
                            dataPenilaian,
                            rerata,
                            kategori
                        )
                    )
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(colorResource(R.color.red))
            ) {
                Text(
                    stringResource(R.string.b_kirim),
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun IconPicker(isError: Boolean) {
    if (isError) {
        Icon(imageVector = Icons.Filled.Warning, contentDescription = null)
    }
}

@Composable
fun ErrorHint(isError: Boolean) {
    if (isError) {
        Text(stringResource(R.string.input_invalid))
    }
}

private fun hitungNilaiMatkul(nilai: Float, bobot: Float): Float {
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

private fun shareData(context: Context, message: String){
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, message)
    }
    if (shareIntent.resolveActivity(context.packageManager) != null){
        context.startActivity(shareIntent)
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