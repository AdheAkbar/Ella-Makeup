package com.example.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.Booking
import com.example.data.MakeupPackage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingFormScreen(
    bookingIdToEdit: Int? = null,
    viewModel: BookingViewModel,
    onNavigateBack: () -> Unit
) {
    val bookings by viewModel.bookings.collectAsStateWithLifecycle()
    val packages by viewModel.activePackages.collectAsStateWithLifecycle()
    
    val bookingToEdit = bookings.find { it.id == bookingIdToEdit }

    var clientName by remember { mutableStateOf(bookingToEdit?.clientName ?: "") }
    var phone by remember { mutableStateOf(bookingToEdit?.phone ?: "") }
    var eventDate by remember { mutableStateOf(bookingToEdit?.eventDate ?: "") }
    var eventTime by remember { mutableStateOf(bookingToEdit?.eventTime ?: "") }
    var location by remember { mutableStateOf(bookingToEdit?.location ?: "") }
    var numberOfPeople by remember { mutableStateOf(bookingToEdit?.numberOfPeople?.toString() ?: "1") }
    var notes by remember { mutableStateOf(bookingToEdit?.notes ?: "") }
    var totalPrice by remember { mutableStateOf(bookingToEdit?.totalPrice?.toString() ?: "") }
    var dpAmount by remember { mutableStateOf(bookingToEdit?.dpAmount?.toString() ?: "0.0") }
    var dpStatus by remember { mutableStateOf(bookingToEdit?.dpStatus ?: "belum") }
    var confirmDeadline by remember { mutableStateOf(bookingToEdit?.confirmDeadline ?: "") }
    
    var selectedPackage: MakeupPackage? by remember { 
        mutableStateOf(packages.find { it.id == bookingToEdit?.packageId }) 
    }
    
    var packageDropdownExpanded by remember { mutableStateOf(false) }
    var dpStatusDropdownExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (bookingIdToEdit == null) "Tambah Booking" else "Edit Booking") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
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
            OutlinedTextField(
                value = clientName,
                onValueChange = { clientName = it },
                label = { Text("Nama Client") },
                modifier = Modifier.fillMaxWidth()
            )
            
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Nomor WA") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = eventDate,
                    onValueChange = { eventDate = it },
                    label = { Text("Tanggal (YYYY-MM-DD)") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = eventTime,
                    onValueChange = { eventTime = it },
                    label = { Text("Jam (HH:MM)") },
                    modifier = Modifier.weight(1f)
                )
            }

            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Lokasi") },
                modifier = Modifier.fillMaxWidth()
            )
            
            // Package Dropdown
            ExposedDropdownMenuBox(
                expanded = packageDropdownExpanded,
                onExpandedChange = { packageDropdownExpanded = it }
            ) {
                OutlinedTextField(
                    value = selectedPackage?.name ?: "Pilih Paket",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Paket Makeup") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = packageDropdownExpanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = packageDropdownExpanded,
                    onDismissRequest = { packageDropdownExpanded = false }
                ) {
                    packages.forEach { pkg ->
                        DropdownMenuItem(
                            text = { Text("${pkg.name} - Rp${pkg.basePrice}") },
                            onClick = {
                                selectedPackage = pkg
                                if (totalPrice.isEmpty()) {
                                    totalPrice = pkg.basePrice.toString()
                                }
                                packageDropdownExpanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = numberOfPeople,
                onValueChange = { numberOfPeople = it },
                label = { Text("Jumlah Orang") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = totalPrice,
                onValueChange = { totalPrice = it },
                label = { Text("Harga Total") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )
            
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = dpAmount,
                    onValueChange = { dpAmount = it },
                    label = { Text("Jumlah DP") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f)
                )
                
                ExposedDropdownMenuBox(
                    expanded = dpStatusDropdownExpanded,
                    onExpandedChange = { dpStatusDropdownExpanded = it },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = dpStatus.capitalize(),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Status DP") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dpStatusDropdownExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = dpStatusDropdownExpanded,
                        onDismissRequest = { dpStatusDropdownExpanded = false }
                    ) {
                        listOf("belum", "lunas").forEach { status ->
                            DropdownMenuItem(
                                text = { Text(status.capitalize()) },
                                onClick = {
                                    dpStatus = status
                                    dpStatusDropdownExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            OutlinedTextField(
                value = confirmDeadline,
                onValueChange = { confirmDeadline = it },
                label = { Text("Deadline Konfirmasi") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Catatan Khusus") },
                minLines = 3,
                maxLines = 5,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val price = totalPrice.toDoubleOrNull() ?: 0.0
                    val dp = dpAmount.toDoubleOrNull() ?: 0.0
                    val persons = numberOfPeople.toIntOrNull() ?: 1
                    
                    if (bookingIdToEdit == null) {
                        viewModel.addBooking(
                            Booking(
                                clientName = clientName,
                                phone = phone,
                                eventDate = eventDate,
                                eventTime = eventTime,
                                location = location,
                                numberOfPeople = persons,
                                packageId = selectedPackage?.id,
                                notes = notes,
                                totalPrice = price,
                                dpAmount = dp,
                                dpStatus = dpStatus,
                                confirmDeadline = confirmDeadline
                            )
                        )
                    } else if (bookingToEdit != null) {
                        viewModel.updateBooking(
                            bookingToEdit.copy(
                                clientName = clientName,
                                phone = phone,
                                eventDate = eventDate,
                                eventTime = eventTime,
                                location = location,
                                numberOfPeople = persons,
                                packageId = selectedPackage?.id,
                                notes = notes,
                                totalPrice = price,
                                dpAmount = dp,
                                dpStatus = dpStatus,
                                confirmDeadline = confirmDeadline
                            )
                        )
                    }
                    onNavigateBack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Simpan Booking")
            }
        }
    }
}
