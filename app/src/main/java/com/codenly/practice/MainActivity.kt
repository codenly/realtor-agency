package com.codenly.practice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.codenly.practice.ui.HomeScreen
import com.codenly.practice.ui.OwnerListScreen
import com.codenly.practice.ui.OwnerEditScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = "home"
            ) {
                composable("home") {
                    HomeScreen(
                        onNavigateToOwners = { navController.navigate("ownerList") }
                    )
                }

                composable("ownerList") {
                    OwnerListScreen(
                        onNavigateToEdit = { ownerId ->
                            val route = if (ownerId == null) "ownerEdit/new" else "ownerEdit/$ownerId"
                            navController.navigate(route)
                        }
                    )
                }

                composable (
                    route = "ownerEdit/{ownerId}",
                    arguments = listOf(navArgument("ownerId") { type = NavType.StringType })
                ){  backStackEntry ->
                    val ownerId = backStackEntry.arguments?.getString("ownerId") ?: "new"
                    OwnerEditScreen(navController = navController)
                }
            }
        }
    }
}
