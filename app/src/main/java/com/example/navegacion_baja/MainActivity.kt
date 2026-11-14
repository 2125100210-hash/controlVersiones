package com.example.navegacion_baja
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.navegacion_baja.navigation.AppNavigation
import com.example.navegacion_baja.ui.theme.Navegacion_BajaTheme

//Rama secundaria
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Navegacion_BajaTheme {
                AppNavigation()
            }
        }
    }
}