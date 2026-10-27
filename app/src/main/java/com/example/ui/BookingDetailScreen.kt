package com.example.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingDetailScreen(
    bookingId: Int,
    viewModel: BookingViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToEdit: (Int) -> Unit
) {
    val bookings by viewModel.bookings.collectAsStateWithLifecycle()
    val packages by viewModel.packages.collectAsStateWithLifecycle()
    val context = LocalContext.current
    
    val booking = bookings.find { it.id == bookingId }
    val pkg = packages.find { it.id == booking?.packageId }

    if (booking == null) {
        onNavigateBack()
        return
    }

    var showDeleteConfirm by remember { mutableStateOf(false) }
    var changeStatusExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Booking") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                actions = {
                    IconButton(onClick = { onNavigateToEdit(booking.id) }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                    IconButton(onClick = { showDeleteConfirm = true }) {
                        Icon(Icons.Default.Delete, contentDescription = "Hapus")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(booking.clientName, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    StatusBadge(status = booking.status)
                    
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    
                    DetailRow("Nomor WA", booking.phone)
                    DetailRow("Tanggal Acara", booking.eventDate)
                    DetailRow("Jam Acara", booking.eventTime)
                    DetailRow("Lokasi", booking.location)
                    DetailRow("Jumlah Orang", "${booking.numberOfPeople} orang")
                    
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    
                    DetailRow("Paket", pkg?.name ?: "Tanpa Paket")
                    DetailRow("Harga Total", "Rp${booking.totalPrice}")
                    DetailRow("DP (${booking.dpStatus})", "Rp${booking.dpAmount}")
                    DetailRow("Sisa Pelunasan", "Rp${booking.totalPrice - booking.dpAmount}")
                    DetailRow("Catatan", booking.notes)
                }
            }

            // Actions
            Button(
                onClick = { changeStatusExpanded = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ubah Status")
            }
            
            DropdownMenu(
                expanded = changeStatusExpanded,
                onDismissRequest = { changeStatusExpanded = false }
            ) {
                listOf("pending", "confirmed", "done", "cancelled").forEach { status ->
                    DropdownMenuItem(
                        text = { Text(status.capitalize()) },
                        onClick = {
                            viewModel.updateBookingStatus(booking.id, status)
                            changeStatusExpanded = false
                        }
                    )
                }
            }

            OutlinedButton(
                onClick = {
                    val message = """
                        Halo ${booking.clientName},
                        Ini adalah reminder untuk booking MUA pada:
                        Tanggal: ${booking.eventDate}
                        Jam: ${booking.eventTime}
                        Lokasi: ${booking.location}
                        Sisa pelunasan: Rp${booking.totalPrice - booking.dpAmount}
                        
                        Terima kasih!
                    """.trimIndent()
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse("https://wa.me/${booking.phone}?text=${Uri.encode(message)}")
                    }
                    context.startActivity(intent)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Kirim Reminder WhatsApp")
            }

            OutlinedButton(
                onClick = {
                    val invoice = """
                        INVOICE BOOKING MUA
                        -------------------
                        Nama: ${booking.clientName}
                        Acara: ${booking.eventDate} ${booking.eventTime}
                        Lokasi: ${booking.location}
                        Paket: ${pkg?.name ?: "-"}
                        Total: Rp${booking.totalPrice}
                        DP Masuk: Rp${booking.dpAmount} (${booking.dpStatus})
                        Sisa: Rp${booking.totalPrice - booking.dpAmount}
                    """.trimIndent()
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TITLE, "Invoice Booking - ${booking.clientName}")
                        putExtra(Intent.EXTRA_TEXT, invoice)
                    }
                    context.startActivity(Intent.createChooser(intent, "Bagikan Invoice"))
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
                Text("Bagikan Invoice Text")
            }
        }

        if (showDeleteConfirm) {
            AlertDialog(
                onDismissRequest = { showDeleteConfirm = false },
                title = { Text("Hapus Booking") },
                text = { Text("Yakin ingin menghapus booking atas nama ${booking.clientName}?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.deleteBooking(booking.id)
                            showDeleteConfirm = false
                            onNavigateBack()
                        }
                    ) {
                        Text("Hapus", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteConfirm = false }) {
                        Text("Batal")
                    }
                }
            )
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
    }
}
