package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookings")
data class Booking(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val clientName: String,
    val phone: String,
    val eventDate: String, // YYYY-MM-DD format
    val eventTime: String, // HH:MM format
    val location: String,
    val numberOfPeople: Int = 1,
    val packageId: Int?,
    val notes: String,
    val totalPrice: Double,
    val dpAmount: Double = 0.0,
    val dpStatus: String = "belum", // belum, lunas
    val confirmDeadline: String,
    val status: String = "pending", // pending, confirmed, done, cancelled
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
