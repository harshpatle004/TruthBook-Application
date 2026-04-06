package com.example.truthbook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.truthbook.core.theme.TruthBookTheme
import com.example.truthbook.navigation.NavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            TruthBookTheme {
                NavGraph()
            }
        }
    }


}