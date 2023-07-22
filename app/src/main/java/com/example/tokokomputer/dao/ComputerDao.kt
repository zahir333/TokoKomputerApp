package com.example.tokokomputer.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.tokokomputer.model.Computer
import kotlinx.coroutines.flow.Flow

@Dao
interface ComputerDao {
    @Query("SELECT * FROM computer_table ORDER BY name ASC")
    fun getALLComputer(): Flow<List<Computer>>

    @Insert
    suspend fun insertComputer(computer: Computer)

    @Delete
    suspend fun deleteComputer(computer: Computer)

    @Update fun updateComputer(computer: Computer)
}