package com.bagas0060.scorecalc.ui.screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bagas0060.scorecalc.database.IPSemesterDao
import com.bagas0060.scorecalc.model.IpSemester
import com.bagas0060.scorecalc.network.NilaiApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(daoIp: IPSemesterDao): ViewModel() {

    val data: StateFlow<List<IpSemester>> = daoIp.getIpSemester().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )
}