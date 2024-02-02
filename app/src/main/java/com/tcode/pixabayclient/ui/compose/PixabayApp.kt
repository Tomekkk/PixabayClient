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

@Composable
fun PixabayNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(route = Screen.Home.route) {
            SearchScreen(onImageClick = {
                navController.navigate(Screen.Details.createRoute(imageId = it))
            })
        }
        composable(route = Screen.Details.route, arguments = Screen.Details.navArguments) {
            DetailsScreen { navController.navigateUp() }
        }
    }
}
