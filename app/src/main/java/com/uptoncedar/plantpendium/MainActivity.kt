package com.uptoncedar.plantpendium

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.uptoncedar.list.ui.PlantListScreen
import com.uptoncedar.plant.details.ui.PlantDetailsScreen
import com.uptoncedar.plantpendium.ui.theme.PlantpendiumTheme
import dagger.hilt.android.AndroidEntryPoint
import com.uptoncedar.plant.details.R as detailsR

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PlantpendiumTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val topBarTitle = when (currentRoute) {
                    "plantList" -> stringResource(R.string.app_name)
                    "plantDetails/{id}" -> stringResource(detailsR.string.title_plant_details)
                    else -> stringResource(R.string.app_name)
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = { Text(topBarTitle) },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                            ),
                            navigationIcon = {
                                if (currentRoute != "plantList") {
                                    IconButton(onClick = { navController.popBackStack() }) {
                                        Icon(
                                            Icons.AutoMirrored.Filled.ArrowBack,
                                            stringResource(android.R.string.ok)
                                        )
                                    }
                                }
                            }
                        )
                    },
                    containerColor = MaterialTheme.colorScheme.background
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "plantList",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("plantList") {
                            PlantListScreen(onNavigateToDetails = {
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