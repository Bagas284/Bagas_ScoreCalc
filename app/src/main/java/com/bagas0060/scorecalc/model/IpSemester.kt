package com.bagas0060.scorecalc.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ipsemester")
data class IpSemester (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val namaPengguna: String,
    val semester: String,
    val prodi: String,
    val mataKuliah: String,
    val sks: Long,
    val indeks: String
)