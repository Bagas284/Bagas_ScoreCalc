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
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bagas0060.scorecalc.R
import com.bagas0060.scorecalc.navigation.Screen
import com.bagas0060.scorecalc.ui.components.MainTopAppBar
import com.bagas0060.scorecalc.ui.theme.ScoreCalcTheme

const val KEY_ID_IPSEMESTER = "ipSemester"

@Composable
fun HitungIPScreen(navController: NavHostController, id: Long? = null) {
    var namaPengguna by rememberSaveable { mutableStateOf("") }
    var selectedOptionText by rememberSaveable { mutableStateOf("") }
    var programStudi by rememberSaveable { mutableStateOf("") }
    var mataKuliah by rememberSaveable { mutableStateOf("") }
    var sks by rememberSaveable { mutableStateOf("") }
    var indeks by rememberSaveable { mutableStateOf("") }

    val viewModel: MainViewModel = viewModel()

    LaunchedEffect(Unit) {
        if (id == null)return@LaunchedEffect
        val data = viewModel.getNilaiIpSemester(id) ?: return@LaunchedEffect
        namaPengguna = data.namaPengguna
        selectedOptionText = data.semester
        programStudi = data.prodi
        mataKuliah = data.mataKuliah
        sks = data.sks.toString()
        indeks = data.indeks
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
                    if (id == null) {
                        Text(
                            text = stringResource(R.string.tambah),
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        Text(
                            text = stringResource(R.string.edit),
                            fontWeight = FontWeight.Bold
                        )
                    }
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
        }
    ) { innerPadding ->
        HitungIPContent(
            userName = namaPengguna,
            onUserNameChange = { namaPengguna = it},
            semester = selectedOptionText,
            onSemesterChange = { selectedOptionText = it},
            studyProgram = programStudi,
            onStudyProgramChange = { programStudi = it},
            subject = mataKuliah,
            onSubjectChange = { mataKuliah = it},
            credit = sks,
            onCreditChange = { sks = it},
            indeks = indeks,
            onIndeksChange = { indeks = it},
            modifier = Modifier.padding(innerPadding),
            navController = navController
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HitungIPContent(
    modifier: Modifier = Modifier,
    userName: String, onUserNameChange: (String) -> Unit,
    semester: String, onSemesterChange: (String) -> Unit,
    studyProgram: String, onStudyProgramChange: (String) -> Unit,
    subject: String, onSubjectChange: (String) -> Unit,
    credit: String, onCreditChange: (String) -> Unit,
    indeks: String, onIndeksChange: (String) -> Unit,
    navController: NavHostController
) {
    // nama pengguna
    var namaPenggunaError by rememberSaveable { mutableStateOf(false) }

    // Semester
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
    var semesterError by rememberSaveable { mutableStateOf(false) }

    // Prodi
    var programStudiError by rememberSaveable { mutableStateOf(false) }

    // Mata kuliah
    var mataKuliahError by rememberSaveable { mutableStateOf(false) }

    // SKS
    var sksError by rememberSaveable { mutableStateOf(false) }

    // Indeks
    var indeksError by rememberSaveable { mutableStateOf(false) }

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
            value = userName,
            onValueChange = { onUserNameChange(it) },
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
                value = semester,
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
                            onSemesterChange(option)
                            expanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                }
            }
        }

        // Input Prodi
        OutlinedTextField(
            value = studyProgram,
            onValueChange = { onStudyProgramChange(it) },
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

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = subject,
                onValueChange = { onSubjectChange(it) },
                label = { Text(stringResource(R.string.labelMataKuliah)) },
                singleLine = true,
                isError = mataKuliahError,
                supportingText = { ErrorHintIp(mataKuliahError) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = credit,
                    onValueChange = { onCreditChange(it) },
                    label = { Text(stringResource(R.string.labelSKS)) },
                    singleLine = true,
                    isError = sksError,
                    supportingText = {
                        if (sksError) {
                            Text(stringResource(R.string.sks_invalid))
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.weight(1f)
                )

                OutlinedTextField(
                    value = indeks,
                    onValueChange = { onIndeksChange(it) },
                    label = { Text(stringResource(R.string.labelIndeks)) },
                    singleLine = true,
                    isError = indeksError,
                    supportingText = { ErrorHintIp(indeksError) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    modifier = Modifier.weight(1f)
                )
            }
        }
        Button(
            onClick = {
                namaPenggunaError =
                    (userName.isBlank() || !userName.matches(Regex("^[a-zA-Z\\s]+$")))
                programStudiError =
                    (studyProgram.isBlank() || !studyProgram.matches(Regex("^[a-zA-Z0-9\\s]+$")))
                semesterError = (semester == "")
                mataKuliahError = (subject.isBlank() || !subject.matches(Regex("^[a-zA-Z\\s]+$")))
                sksError = (credit.isBlank() || !credit.matches(Regex("^[1-6]$")))
                indeksError = (indeks.isBlank() || !indeks.matches(Regex("^[a-eA-E]{1,2}$")))

                if (namaPenggunaError || programStudiError || semesterError || mataKuliahError || sksError || indeksError){
                    return@Button
                }
                navController.popBackStack()
            },
            contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(colorResource(R.color.red)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                stringResource(R.string.b_simpan),
                color = Color.White
            )
        }
    }
}

@Composable
fun IconPickerIp(isError: Boolean) {
    if (isError) {
        Icon(imageVector = Icons.Filled.Warning, contentDescription = null)
    }
}

@Composable
fun ErrorHintIp(isError: Boolean) {
    if (isError) {
        Text(stringResource(R.string.input_invalid))
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