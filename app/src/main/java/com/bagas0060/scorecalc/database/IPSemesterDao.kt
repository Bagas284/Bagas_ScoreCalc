package com.bagas0060.scorecalc.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.bagas0060.scorecalc.model.IpSemester
import kotlinx.coroutines.flow.Flow

@Dao
interface IPSemesterDao {

    @Insert
    suspend fun insert(ipsemester: IpSemester) // menggunakan suspend agar asinkronus

    @Update
    suspend fun update(ipsemester: IpSemester)

    @Query("SELECT * FROM ipsemester ORDER BY semester ASC")
    fun getIpSemester(): Flow<List<IpSemester>> // dengan menggunakan flow, perubahan langsung dapat diketahui

    @Query("SELECT * FROM ipsemester WHERE id = :id")
    suspend fun getIpSemesterById(id: Long): IpSemester?
}