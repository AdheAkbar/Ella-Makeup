package com.example.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.MakeupPackage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PackagesScreen(
    viewModel: BookingViewModel,
    onNavigateBack: () -> Unit
) {
    val packages by viewModel.packages.collectAsStateWithLifecycle()
    
    var showDialog by remember { mutableStateOf(false) }
    var packageToEdit by remember { mutableStateOf<MakeupPackage?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Master Paket Makeup") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { 
                packageToEdit = null
                showDialog = true 
            }) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Paket")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (packages.isEmpty()) {
                item {
                    Text("Belum ada paket.")
                }
            } else {
                items(packages) { pkg ->
                    PackageCard(
                        makeupPackage = pkg,
                        onEdit = {
                            packageToEdit = pkg
                            showDialog = true
                        },
                        onDelete = {
                            viewModel.deletePackage(pkg.id)
                        }
                    )
                }
            }
        }
        
        if (showDialog) {
            PackageDialog(
                makeupPackage = packageToEdit,
                onDismiss = { showDialog = false },
                onSave = { pkg ->
                    if (packageToEdit == null) {
                        viewModel.addPackage(pkg)
                    } else {
                        viewModel.updatePackage(pkg)
                    }
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun PackageCard(
    makeupPackage: MakeupPackage,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(makeupPackage.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Row {
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                    }
                }
            }
            Text("Rp${makeupPackage.basePrice}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(makeupPackage.description, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun PackageDialog(
    makeupPackage: MakeupPackage?,
    onDismiss: () -> Unit,
    onSave: (MakeupPackage) -> Unit
) {
    var name by remember { mutableStateOf(makeupPackage?.name ?: "") }
    var description by remember { mutableStateOf(makeupPackage?.description ?: "") }
    var basePrice by remember { mutableStateOf(makeupPackage?.basePrice?.toString() ?: "") }
    var isActive by remember { mutableStateOf(makeupPackage?.isActive ?: true) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (makeupPackage == null) "Tambah Paket" else "Edit Paket") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nama Paket") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Deskripsi") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = basePrice,
                    onValueChange = { basePrice = it },
                    label = { Text("Harga Dasar") },
                    modifier = Modifier.fillMaxWidth()
                )
                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    Checkbox(checked = isActive, onCheckedChange = { isActive = it })
                    Text("Aktif")
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val price = basePrice.toDoubleOrNull() ?: 0.0
                    if (makeupPackage == null) {
                        onSave(MakeupPackage(name = name, description = description, basePrice = price, isActive = isActive))
                    } else {
                        onSave(makeupPackage.copy(name = name, description = description, basePrice = price, isActive = isActive))
                    }
                }
            ) {
                Text("Simpan")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}
