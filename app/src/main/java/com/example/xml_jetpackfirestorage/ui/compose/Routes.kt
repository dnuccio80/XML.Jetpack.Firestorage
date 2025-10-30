package com.example.xml_jetpackfirestorage.ui.compose

sealed class Routes(val route:String) {
    data object Home:Routes("home")
    data object Gallery:Routes("gallery")
}
