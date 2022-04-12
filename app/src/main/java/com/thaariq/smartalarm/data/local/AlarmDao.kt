package com.thaariq.smartalarm.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.thaariq.smartalarm.data.Alarm

@Dao
interface AlarmDao {

    @Insert
    fun addAlarm(alarm: Alarm)

    @Query("SELECT * FROM alarm")
    fun getAlarm() : LiveData<List<Alarm>>

    @Delete
    fun deleteAlarm (alarm: Alarm)
}