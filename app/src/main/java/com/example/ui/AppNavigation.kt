package com.example.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun AppNavigation(
    navController: NavHostController,
    viewModel: BookingViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "dashboard",
        modifier = modifier
    ) {
        composable("dashboard") {
            DashboardScreen(
                viewModel = viewModel,
                onNavigateToNewBooking = { navController.navigate("booking_form") },
                onNavigateToPackages = { navController.navigate("packages") },
                onNavigateToBookingDetail = { id -> navController.navigate("booking_detail/$id") }
            )
        }
        
        composable("booking_form") {
            BookingFormScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable("booking_detail/{bookingId}") { backStackEntry ->
            val bookingIdString = backStackEntry.arguments?.getString("bookingId")
            val bookingId = bookingIdString?.toIntOrNull()
            
            if (bookingId != null) {
                BookingDetailScreen(
                    bookingId = bookingId,
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToEdit = { id -> navController.navigate("booking_edit/$id") }
                )
            }
        }

        composable("booking_edit/{bookingId}") { backStackEntry ->
            val bookingIdString = backStackEntry.arguments?.getString("bookingId")
            val bookingId = bookingIdString?.toIntOrNull()
            
            if (bookingId != null) {
                BookingFormScreen(
                    bookingIdToEdit = bookingId,
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
        
        composable("packages") {
            PackagesScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
