package com.bagas0060.scorecalc.ui.screen

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

class ApiViewModel: ViewModel() {

    var data = mutableStateOf(emptyList<IpSemesterImage>())
        private set

    var status = MutableStateFlow(ApiStatus.LOADING)
        private set

    init {
        retrieveData()
    }

    fun retrieveData(){
        viewModelScope.launch(Dispatchers.IO) {
            status.value = ApiStatus.LOADING
            try {
                data.value = NilaiApi.service.getNilai()
                status.value = ApiStatus.SUCCESS
            } catch (e: Exception) {
                Log.d("ApiViewModel", "Failure: ${e.message}")
                status.value = ApiStatus.FAILED
            }
        }
    }
}