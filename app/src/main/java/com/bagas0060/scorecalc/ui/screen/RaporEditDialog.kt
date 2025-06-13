package com.bagas0060.scorecalc.ui.screen

import android.content.ContentResolver
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bagas0060.scorecalc.R
import com.bagas0060.scorecalc.model.IpSemesterImage
import com.bagas0060.scorecalc.network.NilaiApi
import com.bagas0060.scorecalc.ui.theme.ScoreCalcTheme
import androidx.compose.ui.platform.LocalContext
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView


@Composable
fun RaporEditDialog(
    ipSemesterImage: IpSemesterImage,
    onDismissRequest: () -> Unit,
    onConfirmation: (String, String, String, Bitmap?) -> Unit
) {
    var semester by remember { mutableStateOf(ipSemesterImage.semester) }
    var mataKuliah by remember { mutableStateOf(ipSemesterImage.mataKuliah) }

    val context = LocalContext.current

    var bitmap: Bitmap? by remember { mutableStateOf(null) }
    val launcher = rememberLauncherForActivityResult(CropImageContract()) {
        bitmap = getCroppedImage(context.contentResolver, it)
    }

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (bitmap != null) {
                    Image(
                        bitmap = bitmap!!.asImageBitmap(),
                        contentDescription = stringResource(R.string.gambar, ipSemesterImage.gambar),
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                    )
                } else {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(NilaiApi.getIpSemesterImageUrl(ipSemesterImage.gambar))
                            .crossfade(true)
                            .build(),
                        contentDescription = stringResource(R.string.gambar, ipSemesterImage.gambar),
                        placeholder = painterResource(R.drawable.loading_img),
                        error = painterResource(id = R.drawable.baseline_broken_image_24),
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                    )
                }

                OutlinedButton(
                    onClick = {
                        val options = CropImageContractOptions(
                            null,
                            CropImageOptions(
                                imageSourceIncludeCamera = true,
                                imageSourceIncludeGallery = true,
                                fixAspectRatio = true,
                            )
                        )
                        launcher.launch(options)
                    },
                    modifier = Modifier.padding(top = 8.dp),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, colorResource(R.color.red)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = colorResource(R.color.red)
                    )
                ) {
                    Text(text = stringResource(R.string.editGambar))
                }

                OutlinedTextField(
                    value = semester,
                    onValueChange = { semester = it },
                    label = { Text(text = stringResource(id = R.string.labelPilihSemester)) },
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.padding(top = 8.dp)
                )

                OutlinedTextField(
                    value = mataKuliah,
                    onValueChange = { mataKuliah = it },
                    label = { Text(text = stringResource(id = R.string.labelMataKuliah)) },
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Done
                    ),
                    modifier = Modifier.padding(top = 8.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    OutlinedButton(
                        onClick = { onDismissRequest() },
                        modifier = Modifier.padding(8.dp),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, colorResource(R.color.red)),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = colorResource(R.color.red)
                        )
                    ) {
                        Text(
                            text = stringResource(id = R.string.tombol_batal),
                            color = colorResource(id = R.color.red)
                        )
                    }

                    Button(
                        onClick = { onConfirmation(ipSemesterImage.id, semester, mataKuliah, bitmap) },
                        enabled = semester.isNotEmpty() && mataKuliah.isNotEmpty(),
                        modifier = Modifier.padding(8.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(id = R.color.red),
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = stringResource(id = R.string.b_simpan),
                        )
                    }
                }
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
fun RaporEditDialogPreview() {
    ScoreCalcTheme {
        RaporEditDialog(
            ipSemesterImage = IpSemesterImage("", "", "", "", 1),
            onDismissRequest = {},
            onConfirmation = { _, _, _, _ -> }
        )
    }
}