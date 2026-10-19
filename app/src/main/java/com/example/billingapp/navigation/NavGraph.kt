package com.example.billingapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.billingapp.database.DatabaseConnect
import com.example.billingapp.screens.BillHistory
import com.example.billingapp.screens.BillInfoScreen
import com.example.billingapp.screens.BillingScreen
import com.example.billingapp.screens.ShopProfile

@Composable
fun NavGraph(database: DatabaseConnect){
    val navController = rememberNavController()

    NavHost(navController = navController,
        startDestination = Routes.Billing){
        composable(Routes.Billing){
            BillingScreen(
                Database = database,
                navigateToHistory= {
                    navController.navigate(Routes.History)
                },
                navigateToSprofile ={navController.navigate(Routes.ShopProfile)}
            )
            }
        composable(Routes.History){
            BillHistory(
                Database = database,
                onBack = {
                    navController.popBackStack()
                },
                navigateToDetails ={billNo->
                    navController.navigate("detailsScreen/${billNo}")
                }
            )
        }
        composable(
            route = Routes.Details,
            arguments = listOf(navArgument("billNo") { type = NavType.LongType })
        ) { backStackEntry ->
            // 4. Extract the ID. Since it cannot be null, fallback safely to 0L just in case
            val billNo = backStackEntry.arguments?.getLong("billNo") ?: 0L

            BillInfoScreen(
                database = database,
                Billno = billNo // 5. Pass it into your screen
            )
        }
        composable(Routes.ShopProfile){
            ShopProfile(database)
        }
        }

    }

