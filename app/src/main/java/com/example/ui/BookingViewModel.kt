package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.Booking
import com.example.data.BookingRepository
import com.example.data.MakeupPackage
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BookingViewModel(private val repository: BookingRepository) : ViewModel() {
    
    init {
        viewModelScope.launch {
            val currentPackages = repository.allPackages.first()
            if (currentPackages.isEmpty()) {
                repository.insertPackage(
                    MakeupPackage(
                        name = "BASIC MAKEUP",
                        description = "Natural makeup\nBulumata regular/request tanpa bulumata\nUntuk foto ijazah, kondangan\nNote: ketahan tidak terlalu lama dan tanpa hijabdo",
                        basePrice = 80000.0
                    )
                )
                repository.insertPackage(
                    MakeupPackage(
                        name = "SOFT GLAM MAKEUP",
                        description = "Coverage lebih tinggi\nMakeup lebih detail\nUntuk acara yearbook, perpisahan, pendamping wisuda, dll.",
                        basePrice = 100000.0
                    )
                )
                repository.insertPackage(
                    MakeupPackage(
                        name = "PREMIUM MAKEUP",
                        description = "Bulumata dan produk premium\nTahan lebih lama\nCocok untuk wisuda, bridesmaid, party, dll.",
                        basePrice = 150000.0
                    )
                )
            }
        }
    }

    val bookings: StateFlow<List<Booking>> = repository.allBookings
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
        
    val packages: StateFlow<List<MakeupPackage>> = repository.allPackages
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
        
    val activePackages: StateFlow<List<MakeupPackage>> = repository.activePackages
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addBooking(booking: Booking) {
        viewModelScope.launch {
            repository.insertBooking(booking)
        }
    }

    fun updateBooking(booking: Booking) {
        viewModelScope.launch {
            repository.updateBooking(booking.copy(updatedAt = System.currentTimeMillis()))
        }
    }

    fun deleteBooking(id: Int) {
        viewModelScope.launch {
            repository.deleteBookingById(id)
        }
    }
    
    fun updateBookingStatus(id: Int, newStatus: String) {
        viewModelScope.launch {
            val booking = repository.getBookingById(id)
            if (booking != null) {
                repository.updateBooking(booking.copy(status = newStatus, updatedAt = System.currentTimeMillis()))
            }
        }
    }

    fun addPackage(makeupPackage: MakeupPackage) {
        viewModelScope.launch {
            repository.insertPackage(makeupPackage)
        }
    }

    fun updatePackage(makeupPackage: MakeupPackage) {
        viewModelScope.launch {
            repository.updatePackage(makeupPackage)
        }
    }

    fun deletePackage(id: Int) {
        viewModelScope.launch {
            repository.deletePackageById(id)
        }
    }
}

class BookingViewModelFactory(private val repository: BookingRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BookingViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
