package com.julm.mitecmi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.julm.mitecmi.navigation.AppNavigation
import com.julm.mitecmi.ui.theme.MiTecmiTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            MiTecmiTheme {

                AppNavigation()
            }
        }
    }
}