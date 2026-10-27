package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "packages")
data class MakeupPackage(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String,
    val basePrice: Double,
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)
