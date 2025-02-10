package com.akoc.perfumeanalyzer

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.akoc.perfumeanalyzer.ui.screens.*
import com.akoc.perfumeanalyzer.ui.theme.PerfumeAnalyzerTheme
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PerfumeAnalyzerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = "home"
                    ) {
                        composable("home") {
                            HomeScreen(
                                onNavigateToCamera = { navController.navigate("camera") },
                                onNavigateToHoroscope = { navController.navigate("horoscope") }
                            )
                        }
                        
                        composable("camera") {
                            CameraScreen(
                                onImageCaptured = { uri ->
                                    val encodedPath = URLEncoder.encode(uri.toString(), StandardCharsets.UTF_8.toString())
                                    navController.navigate("analysis/$encodedPath") {
                                        popUpTo("camera") { inclusive = true }
                                    }
                                },
                                onNavigateBack = {
                                    navController.navigateUp()
                                }
                            )
                        }
                        
                        composable(
                            route = "analysis/{imageUri}",
                            arguments = listOf(
                                navArgument("imageUri") { 
                                    type = NavType.StringType
                                }
                            )
                        ) { backStackEntry ->
                            val encodedUri = backStackEntry.arguments?.getString("imageUri")
                            val decodedUri = encodedUri?.let {
                                URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
                            }
                            val uri = decodedUri?.let { Uri.parse(it) }
                            
                            if (uri != null) {
                                AnalysisScreen(
                                    onBackClick = { 
                                        navController.navigate("home") {
                                            popUpTo("home") { inclusive = true }
                                        }
                                    }
                                )
                            } else {
                                LaunchedEffect(Unit) {
                                    navController.navigate("home") {
                                        popUpTo("home") { inclusive = true }
                                    }
                                }
                            }
                        }

                        composable("horoscope") {
                            HoroscopeScreen(
                                onNavigateBack = { 
                                    navController.navigate("home") {
                                        popUpTo("home") { inclusive = true }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
} 