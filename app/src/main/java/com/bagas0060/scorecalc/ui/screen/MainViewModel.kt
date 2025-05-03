package com.bagas0060.scorecalc.ui.screen

import androidx.lifecycle.ViewModel
import com.bagas0060.scorecalc.model.IpSemester

class MainViewModel: ViewModel() {

    val data = listOf(
        IpSemester(
            1,
            "Bagas Aldianata",
            "Semester 3",
            "D3 RPLA",
            "IJK",
            3,
            "A"
        ),
        IpSemester(
            2,
            "Bagas Aldianata",
            "Semester 3",
            "D3 RPLA",
            "IMA",
            4,
            "A"
        )
    )
}