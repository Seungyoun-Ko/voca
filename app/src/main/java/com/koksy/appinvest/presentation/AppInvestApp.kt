package com.koksy.appinvest.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.koksy.appinvest.presentation.dashboard.DashboardRoute
import com.koksy.appinvest.presentation.navigation.AppBottomNavigationBar
import com.koksy.appinvest.presentation.navigation.AppDestination
import com.koksy.appinvest.presentation.placeholder.PlaceholderScreen
import com.koksy.appinvest.presentation.ranking.RankingRoute
import com.koksy.appinvest.presentation.search.SearchRoute

@Composable
fun AppInvestApp() {
    val navController = rememberNavController()
    val destinations = AppDestination.entries
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry.value?.destination?.route

    Scaffold(
        bottomBar = {
            AppBottomNavigationBar(
                destinations = destinations,
                currentRoute = currentRoute,
                onDestinationClick = { destination ->
                    navController.navigate(destination.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
            )
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppDestination.Dashboard.route,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(AppDestination.Dashboard.route) {
                DashboardRoute()
            }
            composable(AppDestination.Ranking.route) {
                RankingRoute()
            }
            composable(AppDestination.Search.route) {
                SearchRoute()
            }
            composable(AppDestination.News.route) {
                PlaceholderScreen(title = AppDestination.News.label)
            }
            composable(AppDestination.Portfolio.route) {
                PlaceholderScreen(title = AppDestination.Portfolio.label)
            }
        }
    }
}
