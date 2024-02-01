package com.tcode.pixabayclient.ui.compose

sealed class Screen(val route: String) {
    data object Home : Screen("home")
}
