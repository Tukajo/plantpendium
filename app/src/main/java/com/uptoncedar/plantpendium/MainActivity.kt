package com.uptoncedar.plantpendium

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.uptoncedar.list.ui.PlantListScreen
import com.uptoncedar.plant.details.ui.PlantDetailsScreen
import com.uptoncedar.plantpendium.ui.theme.PlantpendiumTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PlantpendiumTheme {

                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize(), topBar = {
                        TopAppBar(
                            title = { Text("Plantpendium") })
                    }) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "plantList",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("plantList") {
                            PlantListScreen(onNavigateToDetails = {
                                // TODO Refactor routes to a cleaner location.
                                val route = "plantDetails/${it}"
                                navController.navigate(route)
                            })
                        }
                        composable(
                            route = "plantDetails/{id}",
                            arguments = listOf(navArgument("id") { type = NavType.StringType })
                        ) { backStackEntry ->
                            backStackEntry.arguments?.getString("id")?.let {
                                PlantDetailsScreen(plantId = it)
                            }
                        }
                    }
                }
            }
        }
    }
}
