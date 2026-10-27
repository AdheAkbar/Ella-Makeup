package com.example

import android.app.Application
import androidx.room.Room
import com.example.data.AppDatabase
import com.example.data.BookingRepository

class BookingApplication : Application() {
    val database by lazy {
        Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "booking_database"
        ).build()
    }
    
    val repository by lazy {
        BookingRepository(database.bookingDao())
    }
}
