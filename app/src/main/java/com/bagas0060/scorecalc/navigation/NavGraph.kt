package com.bagas0060.scorecalc.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bagas0060.scorecalc.ui.screen.AboutScreen
import com.bagas0060.scorecalc.ui.screen.HitungIPScreen
import com.bagas0060.scorecalc.ui.screen.HitungMatkulScreen
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
        composable(route = Screen.Matkul.route){
            HitungMatkulScreen(navController)
        }
        composable (route = Screen.IP.route){
            HitungIPScreen(navController)
        }
    }
}