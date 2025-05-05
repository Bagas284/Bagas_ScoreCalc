package com.bagas0060.scorecalc.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bagas0060.scorecalc.database.IPSemesterDao
import com.bagas0060.scorecalc.model.IpSemester
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailViewModel(private val daoIp : IPSemesterDao) : ViewModel() {

    fun insert(namaPengguna: String, semester: String, prodi: String, mataKuliah: String, sks: Long, indeks: String){
        val ipSemester = IpSemester(
            namaPengguna = namaPengguna,
            semester = semester,
            prodi = prodi,
            mataKuliah = mataKuliah,
            sks = sks,
            indeks = indeks
        )

        viewModelScope.launch (Dispatchers.IO) {
            daoIp.insert(ipSemester)
        }
    }
    suspend fun getNilaiIpSemester(id: Long): IpSemester? {
        return daoIp.getIpSemesterById(id)
    }

    fun update(id: Long, namaPengguna: String, semester: String, prodi: String, mataKuliah: String, sks: Long, indeks: String){
        val ipSemester =IpSemester(
            id = id,
            namaPengguna = namaPengguna,
            semester = semester,
            prodi = prodi,
            mataKuliah = mataKuliah,
            sks = sks,
            indeks = indeks
        )

        viewModelScope.launch(Dispatchers.IO) {
            daoIp.update(ipSemester)
        }
    }
}