package com.langsapp.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.langsapp.main.MainStateController
import com.langsapp.main.UiState
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mainStateController = MainStateController(AndroidMainStateStorage())
        lifecycleScope.launch { mainStateController.init() }

        setContent {
            val navController = rememberNavController()
            val state = mainStateController.uiState.collectAsState()
            NavHost(
                navController = navController,
                startDestination = state.value.toString()
            ) {
                composable(UiState.Welcome.toString()) { WelcomeScreen() }
                composable(UiState.Loading.toString()) { LoadingScreen() }
                composable(UiState.Identity.toString()) { IdentityScreen() }
            }
            navController.navigate(state.value.toString())
        }
    }
}
