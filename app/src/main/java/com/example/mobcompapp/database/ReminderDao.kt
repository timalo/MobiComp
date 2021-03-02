package com.example.mobcompapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction


@Dao
interface ReminderDao {
    @Transaction
    @Insert
    fun insert(paymentInfo: ReminderInfo): Long

    @Query("DELETE FROM reminderInfo WHERE uid = :id")
    fun delete(id: Int)

    @Query("SELECT * FROM reminderInfo")
    fun getPaymentInfos(): List<ReminderInfo>

    @Query("SELECT * FROM reminderInfo WHERE date < :currDate OR (date = :currDate AND time < :currTime)")
    fun getPastReminderInfos(currDate: String, currTime: String): List<ReminderInfo>
}