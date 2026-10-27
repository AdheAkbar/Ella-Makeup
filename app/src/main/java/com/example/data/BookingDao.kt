package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BookingDao {
    // Bookings
    @Query("SELECT * FROM bookings ORDER BY eventDate ASC, eventTime ASC")
    fun getAllBookings(): Flow<List<Booking>>

    @Query("SELECT * FROM bookings WHERE id = :id")
    suspend fun getBookingById(id: Int): Booking?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: Booking)

    @Update
    suspend fun updateBooking(booking: Booking)

    @Query("DELETE FROM bookings WHERE id = :id")
    suspend fun deleteBookingById(id: Int)

    // Packages
    @Query("SELECT * FROM packages ORDER BY name ASC")
    fun getAllPackages(): Flow<List<MakeupPackage>>

    @Query("SELECT * FROM packages WHERE isActive = 1 ORDER BY name ASC")
    fun getActivePackages(): Flow<List<MakeupPackage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPackage(makeupPackage: MakeupPackage)

    @Update
    suspend fun updatePackage(makeupPackage: MakeupPackage)

    @Query("DELETE FROM packages WHERE id = :id")
    suspend fun deletePackageById(id: Int)
}
