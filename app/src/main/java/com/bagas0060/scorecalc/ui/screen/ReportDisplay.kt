package com.bagas0060.scorecalc.ui.screen

import android.content.ContentResolver
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bagas0060.scorecalc.R
import com.bagas0060.scorecalc.model.IpSemesterImage
import com.bagas0060.scorecalc.model.User
import com.bagas0060.scorecalc.network.ApiStatus
import com.bagas0060.scorecalc.network.NilaiApi
import com.bagas0060.scorecalc.network.UserDataStore
import com.bagas0060.scorecalc.ui.components.MainTopAppBar
import com.bagas0060.scorecalc.ui.theme.ScoreCalcTheme
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView

@Composable
fun ReportDisplay(navController: NavHostController) {
    val context = LocalContext.current
    var showRaporDialog by remember { mutableStateOf(false) }

    val viewModel: ApiViewModel = viewModel()
    val errorMessage by viewModel.errorMessage

    val dataStore = UserDataStore(context)
    val user by dataStore.userFlow.collectAsState(User())

    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedHewanId by remember { mutableStateOf("") }

    var bitmap: Bitmap? by remember { mutableStateOf(null) }
    val launcher = rememberLauncherForActivityResult(CropImageContract()) {
        bitmap = getCroppedImage(context.contentResolver, it)
        if (bitmap != null) showRaporDialog = true
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
                        text = stringResource(R.string.app_name),
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val options = CropImageContractOptions(
                        null, CropImageOptions(
                            imageSourceIncludeGallery = false,
                            imageSourceIncludeCamera = true,
                            fixAspectRatio = true
                        )
                    )
                    launcher.launch(options)
                },
                containerColor = colorResource(R.color.red)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.tambah_rapor),
                    tint = Color.White
                )
            }
        }
    ) { innerPadding ->
        ReportDisplayContent(
            viewModel,
            user.email,
            Modifier.padding(innerPadding),
            onDelete = { id ->
                selectedHewanId = id
                showDeleteDialog = true
            }
        )

        if (showRaporDialog) {
            RaporDialog(
                bitmap = bitmap,
                onDismissRequest = { showRaporDialog = false }) { semester, mataKuliah ->
                viewModel.saveData(user.email, semester, mataKuliah, bitmap!!)
                showRaporDialog = false
            }
        }

        if (errorMessage != null) {
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            viewModel.clearMessage()
        }

        if (showDeleteDialog) {
            DialogHapus(
                onDismissRequest = { showDeleteDialog = false },
                onConfirmation = {
                    viewModel.deleteData(user.email, selectedHewanId)
                    showDeleteDialog = false
                }
            )
        }
    }
}

@Composable
fun ReportDisplayContent(
    viewModel: ApiViewModel,
    email: String,
    modifier: Modifier = Modifier,
    onDelete: (String) -> Unit
) {
    val data by viewModel.data
    val status by viewModel.status.collectAsState()

    val context = LocalContext.current
    val dataStore = UserDataStore(context)
    val user by dataStore.userFlow.collectAsState(User())
    val isUserReady = user.email.isNotEmpty()

    LaunchedEffect(isUserReady) {
        if (isUserReady) {
            viewModel.retrieveData(user.email)
        }
    }
    when (status) {
        ApiStatus.LOADING -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        ApiStatus.SUCCESS -> {
            LazyVerticalGrid(
                modifier = modifier
                    .fillMaxSize()
                    .padding(4.dp),
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(data.size) { index ->
                    val nilai = data[index]
                    ListItem(
                        ipsemesterimage = nilai,
                        onDeleteClick = onDelete,
                        showDeleteButton = (nilai.mine == 1)
                    )
                }
            }
        }

        ApiStatus.FAILED -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = stringResource(id = R.string.error))

                Button(
                    onClick = { viewModel.retrieveData(email) },
                    modifier = Modifier.padding(top = 16.dp),
                    contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(colorResource(R.color.red))
                ) {
                    Text(text = stringResource(id = R.string.try_again))
                }
            }
        }
    }
}

@Composable
fun ListItem(
    ipsemesterimage: IpSemesterImage,
    onDeleteClick: (String) -> Unit,
    showDeleteButton: Boolean = false
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(320.dp)
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(NilaiApi.getIpSemesterImageUrl(ipsemesterimage.gambar))
                        .crossfade(true)
                        .build(),
                    contentDescription = stringResource(R.string.gambar, ipsemesterimage.gambar),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.loading_img),
                    error = painterResource(id = R.drawable.baseline_broken_image_24),
                    modifier = Modifier
                        .fillMaxWidth()
                )
                if (showDeleteButton) {
                    IconButton(
                        onClick = { onDeleteClick(ipsemesterimage.id) },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = stringResource(id = R.string.hapus),
                            tint = Color.White
                        )
                    }
                }

                IconButton(
                    onClick = { /* TODO: Aksi edit */ },
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = stringResource(id = R.string.edit),
                        tint = Color.White
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = ipsemesterimage.semester,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = ipsemesterimage.mataKuliah,
                    fontSize = 14.sp
                )
            }
        }
    }
}

private fun getCroppedImage(
    resolver: ContentResolver,
    result: CropImageView.CropResult
): Bitmap? {
    if (!result.isSuccessful) {
        Log.e("IMAGE", "Error: ${result.error}")
        return null
    }
    val uri = result.uriContent ?: return null

    return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
        MediaStore.Images.Media.getBitmap(resolver, uri)
    } else {
        val source = ImageDecoder.createSource(resolver, uri)
        ImageDecoder.decodeBitmap(source)
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun ReportDisplayPreview() {
    ScoreCalcTheme {
        ReportDisplay(rememberNavController())
    }
}