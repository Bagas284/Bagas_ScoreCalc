package com.bagas0060.scorecalc.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bagas0060.scorecalc.ui.screen.AboutScreen
import com.bagas0060.scorecalc.ui.screen.CertificationDisplay
import com.bagas0060.scorecalc.ui.screen.DisplayIpSemester
import com.bagas0060.scorecalc.ui.screen.HitungIPScreen
import com.bagas0060.scorecalc.ui.screen.KEY_ID_IPSEMESTER
import com.bagas0060.scorecalc.ui.screen.MainScreen

@Composable
fun SetupNavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ){
        composable(route = Screen.Home.route) {
            MainScreen(navController)
        }
        composable(route = Screen.About.route) {
            AboutScreen(navController)
        }
        composable (route = Screen.FormIpSemesterBaru.route){
            HitungIPScreen(navController)
        }
        composable(
            route = Screen.FormIpSemesterUbah.route,
            arguments = listOf(
                navArgument(KEY_ID_IPSEMESTER) { type = NavType.LongType }
            )
        ) { navBackStackEntry ->
            val id = navBackStackEntry.arguments?.getLong(KEY_ID_IPSEMESTER)
            HitungIPScreen(navController, id)
        }
        composable (route = Screen.DisplayIpSemester.route){
            DisplayIpSemester(navController)
        }
        composable(route = Screen.CertificationDisplay.route) {
            CertificationDisplay(navController)
        }
    }
}