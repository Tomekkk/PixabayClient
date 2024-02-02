package com.tcode.pixabayclient.ui.compose

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(val route: String, val navArguments: List<NamedNavArgument> = emptyList()) {
    data object Home : Screen("home")

    data object Details : Screen(
        route = "imageDetails/{imageId}",
        navArguments =
            listOf(
                navArgument("imageId") {
                    type = NavType.LongType
                },
            ),
    ) {
        fun createRoute(imageId: Long) = "imageDetails/$imageId"
    }
}
