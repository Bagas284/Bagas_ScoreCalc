package com.bagas0060.scorecalc.ui.screen

import androidx.lifecycle.ViewModel
import com.bagas0060.scorecalc.model.IpSemester

class MainViewModel: ViewModel() {

    val data = listOf(
        IpSemester(
            1,
            "Bagas Aldianata",
            "3",
            "D3 RPLA",
            "IJK",
            3,
            "A"
        ),
        IpSemester(
            2,
            "Bagas Aldianata",
            "3",
            "D3 RPLA",
            "IMA",
            4,
            "A"
        ),
        IpSemester(
            3,
            "Daffa Akhadi",
            "3",
            "D3 RPLA",
            "PBO",
            4,
            "A"
        ),
        IpSemester(
            4,
            "Daffa Akhadi",
            "3",
            "D3 RPLA",
            "PBO",
            4,
            "A"
        ),
        IpSemester(
            5,
            "Farhan",
            "4",
            "D3 RPLA",
            "PBO",
            4,
            "A"
        )
    )

    fun getNilaiIpSemester(id: Long): IpSemester? {
        return data.find { it.id == id }
    }
}