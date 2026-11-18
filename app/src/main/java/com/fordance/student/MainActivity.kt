package com.fordance.student

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.fordance.student.core.theme.ForDanceTheme
import com.fordance.student.navigation.ForDanceNavGraph
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ForDanceTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    ForDanceNavGraph()
                }
            }
        }
    }
}
