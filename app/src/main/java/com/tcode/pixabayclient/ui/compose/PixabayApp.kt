package com.tcode.pixabayclient.ui.compose

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun PixabayApp() {
    val navController = rememberNavController()
    PixabayNavHost(navController = navController)
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun PixabayNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(route = Screen.Home.route) {
            SearchList()
        }
    }
}
