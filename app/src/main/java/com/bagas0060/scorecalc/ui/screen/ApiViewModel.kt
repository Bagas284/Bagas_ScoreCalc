package com.bagas0060.scorecalc.ui.screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bagas0060.scorecalc.network.NilaiApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ApiViewModel: ViewModel() {

    init {
        retrieveData()
    }

    private fun retrieveData(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = NilaiApi.service.getNilai()
                Log.d("ApiViewModel", "Success: $result")
            } catch (e: Exception) {
                Log.d("ApiViewModel", "Failure: ${e.message}")
            }
        }
    }
}