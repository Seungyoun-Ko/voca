package com.koksy.appinvest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.koksy.appinvest.presentation.AppInvestApp
import com.koksy.appinvest.ui.theme.AppInvestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppInvestTheme {
                AppInvestApp()
            }
        }
    }
}
