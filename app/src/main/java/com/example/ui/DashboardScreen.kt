package com.example.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.Booking
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: BookingViewModel,
    onNavigateToNewBooking: () -> Unit,
    onNavigateToPackages: () -> Unit,
    onNavigateToBookingDetail: (Int) -> Unit
) {
    val bookings by viewModel.bookings.collectAsStateWithLifecycle()
    
    val pendingCount = bookings.count { it.status == "pending" }
    val revenue = bookings.filter { it.status == "done" || it.status == "confirmed" }.sumOf { it.totalPrice }
    val dpUnpaid = bookings.filter { it.dpStatus == "belum" && it.status != "cancelled" }.sumOf { it.dpAmount }

    val localeID = Locale.Builder().setLanguage("id").setRegion("ID").build()
    val formatRupiah = NumberFormat.getCurrencyInstance(localeID)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        Text("Ella Makeup", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.primary)
                        Text("DASHBOARD PRO", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                ),
                actions = {
                    IconButton(onClick = onNavigateToPackages) {
                        Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Master Paket")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToNewBooking, containerColor = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary) {
                Icon(Icons.Default.Add, contentDescription = "Add Booking")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    SummaryCard(
                        modifier = Modifier.weight(1f),
                        title = "BOOKING BARU",
                        value = pendingCount.toString(),
                        color = MaterialTheme.colorScheme.primary
                    )
                    SummaryCard(
                        modifier = Modifier.weight(1f),
                        title = "ESTIMASI RP",
                        value = formatRupiah.format(revenue).replace(",00",""),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                SummaryCard(
                    modifier = Modifier.fillMaxWidth(),
                    title = "DP BELUM LUNAS",
                    value = formatRupiah.format(dpUnpaid).replace(",00",""),
                    color = MaterialTheme.colorScheme.errorContainer
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text("JADWAL TERDEKAT", style = MaterialTheme.typography.labelMedium)
            }

            val upcomingBookings = bookings.filter { it.status == "pending" || it.status == "confirmed" }
            if (upcomingBookings.isEmpty()) {
                item {
                    Text("Belum ada booking mendatang.", modifier = Modifier.padding(16.dp))
                }
            } else {
                items(upcomingBookings) { booking ->
                    BookingCard(
                        booking = booking,
                        onClick = { onNavigateToBookingDetail(booking.id) }
                    )
                }
            }
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun SummaryCard(modifier: Modifier = Modifier, title: String, value: String, color: Color) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.labelSmall)
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, style = MaterialTheme.typography.headlineMedium)
        }
    }
}

@Composable
fun BookingCard(booking: Booking, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(booking.clientName, style = MaterialTheme.typography.titleLarge)
                    StatusBadge(status = booking.status)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("Tanggal: ${booking.eventDate} • Jam: ${booking.eventTime}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
fun StatusBadge(status: String) {
    val (color, text) = when (status) {
        "pending" -> MaterialTheme.colorScheme.tertiary to "Pending"
        "confirmed" -> MaterialTheme.colorScheme.primary to "Confirmed"
        "done" -> MaterialTheme.colorScheme.secondary to "Selesai"
        "cancelled" -> MaterialTheme.colorScheme.error to "Batal"
        else -> MaterialTheme.colorScheme.onSurface to status.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
    }
    Surface(
        color = color,
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = text,
            color = contentColorFor(color),
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}
