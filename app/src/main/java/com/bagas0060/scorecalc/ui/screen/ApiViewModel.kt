package com.bagas0060.scorecalc.ui.screen

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bagas0060.scorecalc.model.IpSemesterImage
import com.bagas0060.scorecalc.network.ApiStatus
import com.bagas0060.scorecalc.network.NilaiApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream

class ApiViewModel: ViewModel() {

    var data = mutableStateOf(emptyList<IpSemesterImage>())
        private set

    var status = MutableStateFlow(ApiStatus.LOADING)
        private set

    var errorMessage = mutableStateOf<String?>(null)
        private set

    fun retrieveData(email: String){
        viewModelScope.launch(Dispatchers.IO) {
            status.value = ApiStatus.LOADING
            try {
                data.value = NilaiApi.service.getNilai(email)
                status.value = ApiStatus.SUCCESS
            } catch (e: Exception) {
                Log.d("ApiViewModel", "Failure: ${e.message}")
                status.value = ApiStatus.FAILED
            }
        }
    }

    fun saveData(email: String, semester: String, mataKuliah: String, bitmap: Bitmap){
        viewModelScope.launch(Dispatchers.IO){
            try {
                val result = NilaiApi.service.tambahNilai(
                    email,
                    semester.toRequestBody("text/plain".toMediaTypeOrNull()),
                    mataKuliah.toRequestBody("text/plain".toMediaTypeOrNull()),
                    bitmap.toMultipartBody()
                )
                if (result.status == "success")
                    retrieveData(email)
                else
                    throw Exception(result.message)
            } catch (e: Exception) {
                Log.d("ApiViewModel", "Failure: ${e.message}")
                errorMessage.value = "Error: ${e.message}"
            }
        }
    }

    fun deleteData(email: String, id: String) {
        viewModelScope.launch(Dispatchers.IO){
            try {
                val result = NilaiApi.service.hapusNilai(email, id)
                if (result.status == "success")
                    retrieveData(email)

                else
                    throw Exception(result.message)
            } catch (e: Exception) {
                Log.d("MainViewModel", "Failure: ${e.message}")
                errorMessage.value = "Error: ${e.message}"
            }
        }
    }

    private fun Bitmap.toMultipartBody(): MultipartBody.Part {
        val stream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, 80, stream)
        val byteArray = stream.toByteArray()
        val requestBody = byteArray.toRequestBody(
            "image/jpg".toMediaTypeOrNull(), 0, byteArray.size)
        return MultipartBody.Part.createFormData(
            "gambar", "image.jpg", requestBody)
    }

    fun clearMessage() { errorMessage.value = null}
}