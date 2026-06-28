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
import com.codenly.practice.ui.TypeListScreen
import com.codenly.practice.ui.TypeEditScreen
//import com.codenly.practice.ui.ClientListScreen
//import com.codenly.practice.ui.ClientEditScreen
//import com.codenly.practice.ui.RealtorListScreen
//import com.codenly.practice.ui.RealtorEditScreen
//import com.codenly.practice.ui.PropertyListScreen
//import com.codenly.practice.ui.PropertyEditScreen
//import com.codenly.practice.ui.ViewListScreen
//import com.codenly.practice.ui.ViewEditScreen
//import com.codenly.practice.ui.DealListScreen
//import com.codenly.practice.ui.DealEditScreen

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
                        onNavigateToOwners = { navController.navigate("ownerList") },
                        onNavigateToTypes = { navController.navigate("typeList") }
                    )
                }
//,
//onNavigateToRealtors = { navController.navigate(realtorList) },
//onNavigateToProperties = { navController.navigate(propertyList) },
//onNavigateToViews = { navController.navigate(viewList) },
//onNavigateToDeals = { navController.navigate(dealList) }
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

                composable("typeList") {
                    TypeListScreen(
                        onNavigateToEdit = { typeId ->
                            val route = if (typeId == null) "typeEdit/new" else "typeEdit/$typeId"
                            navController.navigate(route)
                        }
                    )
                }

                composable (
                    route = "typeEdit/{typeId}",
                    arguments = listOf(navArgument("typeId") { type = NavType.StringType })
                ){  backStackEntry ->
                    val typeId = backStackEntry.arguments?.getString("typeId") ?: "new"
                    TypeEditScreen(navController = navController)
                }

//                composable("clientList") {
//                    ClientListScreen(
//                        onNavigateToEdit = { clientId ->
//                            val route = if (clientId == null) "clientEdit/new" else "clientEdit/$clientId"
//                            navController.navigate(route)
//                        }
//                    )
//                }
//
//                composable (
//                    route = "clientEdit/{clientId}",
//                    arguments = listOf(navArgument("clientId") { type = NavType.StringType })
//                ){  backStackEntry ->
//                    val clientId = backStackEntry.arguments?.getString("clientId") ?: "new"
//                    ClientEditScreen(navController = navController)
//                }
//
//                composable("realtorList") {
//                    RealtorListScreen(
//                        onNavigateToEdit = { realtorId ->
//                            val route = if (realtorId == null) "realtorEdit/new" else "realtorEdit/$realtorId"
//                            navController.navigate(route)
//                        }
//                    )
//                }
//
//                composable (
//                    route = "realtorEdit/{realtorId}",
//                    arguments = listOf(navArgument("realtorId") { type = NavType.StringType })
//                ){  backStackEntry ->
//                    val realtorId = backStackEntry.arguments?.getString("realtorId") ?: "new"
//                    RealtorEditScreen(navController = navController)
//                }
//
//                composable("propertyList") {
//                    PropertyListScreen(
//                        onNavigateToEdit = { propertyId ->
//                            val route = if (propertyId == null) "propertyEdit/new" else "propertyEdit/$propertyId"
//                            navController.navigate(route)
//                        }
//                    )
//                }
//
//                composable (
//                    route = "propertyEdit/{propertyId}",
//                    arguments = listOf(navArgument("propertyId") { type = NavType.StringType })
//                ){  backStackEntry ->
//                    val propertyId = backStackEntry.arguments?.getString("propertyId") ?: "new"
//                    PropertyEditScreen(navController = navController)
//                }
//
//                composable("viewList") {
//                    ViewListScreen(
//                        onNavigateToEdit = { viewId ->
//                            val route = if (viewId == null) "viewEdit/new" else "viewEdit/$viewId"
//                            navController.navigate(route)
//                        }
//                    )
//                }
//
//                composable (
//                    route = "viewEdit/{viewId}",
//                    arguments = listOf(navArgument("viewId") { type = NavType.StringType })
//                ){  backStackEntry ->
//                    val viewId = backStackEntry.arguments?.getString("viewId") ?: "new"
//                    ViewEditScreen(navController = navController)
//                }
//
//                composable("dealList") {
//                    DealListScreen(
//                        onNavigateToEdit = { dealId ->
//                            val route = if (dealId == null) "dealEdit/new" else "dealEdit/$dealId"
//                            navController.navigate(route)
//                        }
//                    )
//                }
//
//                composable (
//                    route = "dealEdit/{dealId}",
//                    arguments = listOf(navArgument("dealId") { type = NavType.StringType })
//                ){  backStackEntry ->
//                    val dealId = backStackEntry.arguments?.getString("dealId") ?: "new"
//                    DealEditScreen(navController = navController)
//                }
            }
        }
    }
}
