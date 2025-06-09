package com.bagas0060.scorecalc.ui.screen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bagas0060.scorecalc.model.IpSemesterImage
import com.bagas0060.scorecalc.network.NilaiApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ApiViewModel: ViewModel() {

    var data = mutableStateOf(emptyList<IpSemesterImage>())
        private set

    init {
        retrieveData()
    }

    private fun retrieveData(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
               data.value = NilaiApi.service.getNilai()
            } catch (e: Exception) {
                Log.d("ApiViewModel", "Failure: ${e.message}")
            }
        }
    }
}