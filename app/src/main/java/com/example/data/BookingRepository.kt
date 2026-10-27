package com.example.data

import kotlinx.coroutines.flow.Flow

class BookingRepository(private val bookingDao: BookingDao) {
    val allBookings: Flow<List<Booking>> = bookingDao.getAllBookings()
    val allPackages: Flow<List<MakeupPackage>> = bookingDao.getAllPackages()
    val activePackages: Flow<List<MakeupPackage>> = bookingDao.getActivePackages()

    suspend fun getBookingById(id: Int): Booking? = bookingDao.getBookingById(id)
    
    suspend fun insertBooking(booking: Booking) = bookingDao.insertBooking(booking)
    suspend fun updateBooking(booking: Booking) = bookingDao.updateBooking(booking)
    suspend fun deleteBookingById(id: Int) = bookingDao.deleteBookingById(id)

    suspend fun insertPackage(makeupPackage: MakeupPackage) = bookingDao.insertPackage(makeupPackage)
    suspend fun updatePackage(makeupPackage: MakeupPackage) = bookingDao.updatePackage(makeupPackage)
    suspend fun deletePackageById(id: Int) = bookingDao.deletePackageById(id)
}
