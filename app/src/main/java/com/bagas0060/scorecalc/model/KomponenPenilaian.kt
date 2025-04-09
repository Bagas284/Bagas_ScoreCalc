package com.bagas0060.scorecalc.model

data class KomponenPenilaian(
    val nama: String = "",
    val nilai: String = "",
    val bobot: String = "",

    val sks: String = "",
    val indeks: String = "",

    val namaError: Boolean = false,
    val nilaiError: Boolean = false,
    val bobotError: Boolean = false,

    val sksError: Boolean = false,
    val indeksError: Boolean= false
)
