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
fun HitungIPScreen(navController: NavHostController) {
    val komponenList = rememberSaveable(
        saver = listSaver(
            save = { list -> list.map { listOf(it.nama, it.sks, it.indeks, it.namaError, it.sksError, it.indeksError) } },
            restore = { restored ->
                restored.map {
                    KomponenPenilaian(
                        nama = it[0] as String,
                        sks = it[1] as String,
                        indeks = it[2] as String,
                        namaError = it[3] as Boolean,
                        sksError = it[4] as Boolean,
                        indeksError = it[5] as Boolean
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
                        text = stringResource(R.string.hitung_ip),
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
        HitungIPContent(
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
fun HitungIPContent(
    modifier: Modifier = Modifier,
    komponenList: List<KomponenPenilaian>,
    onUpdateKomponen: (Int, KomponenPenilaian) -> Unit
) {
    var namaPengguna by rememberSaveable { mutableStateOf("") }
    var namaPenggunaError by rememberSaveable { mutableStateOf(false) }

    var programStudi by rememberSaveable { mutableStateOf("") }
    var programStudiError by rememberSaveable { mutableStateOf(false) }

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

    var totalIp by rememberSaveable { mutableFloatStateOf(0f)}

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
            trailingIcon = { IconPickerIp(namaPenggunaError) },
            supportingText = { ErrorHintIp(namaPenggunaError) },
            isError = namaPenggunaError,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        // Dropdown Semester
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

        // Input Prodi
        OutlinedTextField(
            value = programStudi,
            onValueChange = { programStudi = it },
            label = { Text(stringResource(R.string.labelProgramStudi)) },
            trailingIcon = { IconPickerIp(programStudiError) },
            supportingText = { ErrorHintIp(programStudiError) },
            isError = programStudiError,
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
                        onUpdateKomponen(index, komponen.copy(nama = it))
                    },
                    label = { Text(stringResource(R.string.labelMataKuliah)) },
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
                        value = komponen.sks,
                        onValueChange = {
                            onUpdateKomponen(
                                index, komponen.copy(
                                    sks = it
                                )
                            )
                        },
                        label = { Text(stringResource(R.string.labelSKS)) },
                        singleLine = true,
                        isError = komponen.sksError,
                        supportingText = {
                            if (komponen.sksError) {
                                Text(stringResource(R.string.input_invalid))
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = komponen.indeks,
                        onValueChange = {
                            onUpdateKomponen(
                                index, komponen.copy(
                                    indeks = it
                                )
                            )
                        },
                        label = { Text(stringResource(R.string.labelIndeks)) },
                        singleLine = true,
                        isError = komponen.indeksError,
                        supportingText = {
                            if(komponen.indeksError){
                                Text(stringResource(R.string.input_invalid))
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
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
                namaPenggunaError = (namaPengguna.isBlank() || !namaPengguna.matches(Regex("^[a-zA-Z\\s]+$")))
                programStudiError = (programStudi.isBlank() || !programStudi.matches(Regex("^[a-zA-Z0-9\\s]+$")))
                semesterError = (selectedOptionText == "")

                var totalSksIndeks = 0f
                var totalSKS = 0f
                var rumusHitungIp = 0f

                var valid = true
                komponenList.forEachIndexed {index,  komponen ->
                    val namaValid = komponen.nama.isNotBlank() && komponen.nama.matches(Regex("^[a-zA-Z0-9\\s]+\$"))
                    val sks = komponen.sks.toFloatOrNull()
                    val indeks = komponen.indeks
                    val indeksValid = indeks.matches(Regex("^[a-eA-E]{1,2}$"))

                    onUpdateKomponen(
                        index, komponen.copy(
                            namaError = !namaValid,
                            sksError = sks == null,
                            indeksError = !indeksValid
                        )
                    )

                    if (namaValid && sks != null && indeksValid) {
                        totalSksIndeks += hitungIP(sks, indeks)
                        totalSKS += sks
                    } else {
                        valid = false
                    }
                    rumusHitungIp = totalSksIndeks / totalSKS
                }
                if(namaPenggunaError || programStudiError || semesterError || !valid) return@Button

                totalIp = rumusHitungIp
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

        if (totalIp != 0f) {
            Text(
                text = stringResource(R.string.ipSemester, totalIp),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)
            )

            Button(
                onClick = {
                    val dataPenilaian = komponenList.joinToString ("\n"){ komponen ->
                        "- ${komponen.nama}: SKS ${komponen.sks}, Indeks ${komponen.indeks}"
                    }
                    shareData (
                        context = context,
                        message = context.getString(
                            R.string.bagikan_ip,
                            namaPengguna,
                            selectedOptionText,
                            programStudi,
                            dataPenilaian,
                            totalIp
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
fun IconPickerIp(isError: Boolean){
    if(isError){
        Icon(imageVector = Icons.Filled.Warning, contentDescription = null)
    }
}

@Composable
fun ErrorHintIp(isError: Boolean) {
    if (isError) {
        Text(stringResource(R.string.input_invalid))
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

private fun hitungIP(sks: Float, indeks: String): Float{
    val nam = kategoriIndeks(indeks)
    return (sks * nam)
}

private fun kategoriIndeks(indeks: String):Float{
    return when (indeks.uppercase()){
        "A" -> 4.0f
        "AB" -> 3.5f
        "B" -> 3.0f
        "BC" -> 2.5f
        "C" -> 2.0f
        "D" -> 1.0f
        "E" -> 0f
        else -> 0f
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun HitungIPScreenPreview() {
    ScoreCalcTheme {
        HitungIPScreen(rememberNavController())
    }
}