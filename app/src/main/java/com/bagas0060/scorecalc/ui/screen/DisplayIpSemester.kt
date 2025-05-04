package com.bagas0060.scorecalc.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bagas0060.scorecalc.R
import com.bagas0060.scorecalc.model.IpSemester
import com.bagas0060.scorecalc.navigation.Screen
import com.bagas0060.scorecalc.ui.components.MainTopAppBar
import com.bagas0060.scorecalc.ui.theme.ScoreCalcTheme

@Composable
fun DisplayIpSemester(navController: NavHostController) {
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
                        text = stringResource(R.string.app_name),
                        fontWeight = FontWeight.Bold
                    )
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.FormIpSemesterBaru.route)
                },
                containerColor = colorResource(R.color.red),
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(R.string.tambah),
                    tint = Color.White
                )
            }
        }
    ) { innerPadding ->
        DisplayIpSemesterContent(Modifier.padding(innerPadding), navController)
    }
}

@Composable
fun DisplayIpSemesterContent(modifier: Modifier = Modifier, navController: NavHostController) {
    val viewModel: MainViewModel = viewModel()
    val data = viewModel.data
//    val data = emptyList<IpSemester>()

    val groupedData = data.groupBy { Triple(it.namaPengguna, it.semester, it.prodi) }

    if (data.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(stringResource(id = R.string.list_kosong))
        }
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 84.dp)
        ) {
            groupedData.forEach { (key, ipList) ->
                val (nama, semester, prodi) = key

                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFF0F0F0)
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                        ) {
                            Text(
                                text = "Semester $semester",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF333333)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 12.dp),
                                textAlign = TextAlign.Center
                            )

                            HorizontalDivider(
                                thickness = 1.dp,
                                color = Color.Gray.copy(alpha = 0.3f)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Nama: $nama",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = Color.DarkGray
                                )
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "Program Studi: $prodi",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = Color.DarkGray
                                )
                            )
                        }
                    }

                }

                items(ipList) {
                    ListItemIpSemester(ipSemester = it) {
                        navController.navigate(Screen.FormIpSemesterUbah.withId(it.id))
                    }
                    HorizontalDivider(color = Color.LightGray, thickness = 1.dp)
                }

                item {
                    HorizontalDivider(color = Color.LightGray, thickness = 6.dp)
                }
            }
        }
    }
}

@Composable
fun ListItemIpSemester(ipSemester: IpSemester, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 7.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(text = "Mata Kuliah: ${ipSemester.mataKuliah}", fontWeight = FontWeight.Bold)
        Text(text = "SKS: ${ipSemester.sks}")
        Text(text = "Indeks: ${ipSemester.indeks}")
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun DisplayIpSemesterPreview() {
    ScoreCalcTheme {
        DisplayIpSemester(rememberNavController())
    }
}