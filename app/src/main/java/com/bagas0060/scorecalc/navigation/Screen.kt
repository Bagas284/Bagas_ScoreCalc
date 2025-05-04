package com.bagas0060.scorecalc.navigation

import com.bagas0060.scorecalc.ui.screen.KEY_ID_IPSEMESTER

sealed class Screen (val route: String) {
    data object Home: Screen("mainScreen")
    data object About: Screen("aboutScreen")
    data object Matkul: Screen("hitungMatkulScreen")
    data object DisplayIpSemester: Screen ("displayIpSemester")
    data object FormIpSemesterBaru: Screen("hitungIPScreen")
    data object FormIpSemesterUbah: Screen("hitungIPScreen/{$KEY_ID_IPSEMESTER}") {
        fun withId(id: Long) = "hitungIPScreen/$id"
    }
}