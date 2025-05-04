package com.bagas0060.scorecalc.util

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bagas0060.scorecalc.database.NilaiDb
import com.bagas0060.scorecalc.ui.screen.MainViewModel

class ViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory{
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val daoIp = NilaiDb.getInstance(context).daoIp
        if (modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel(daoIp) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}