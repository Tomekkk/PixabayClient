package com.tcode.pixabayclient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.tcode.pixabayclient.ui.compose.PixabayApp
import com.tcode.pixabayclient.ui.theme.PixabayClientTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PixabayClientTheme {
                // A surface container using the 'background' color from the theme
                PixabayApp()
            }
        }
    }
}
