package com.bagas0060.scorecalc.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bagas0060.scorecalc.model.IpSemester

@Database(entities = [IpSemester::class], version = 1, exportSchema = false)
abstract class NilaiDb : RoomDatabase() {

    abstract val daoIp: IPSemesterDao

    companion object {

        @Volatile
        private var INSTANCE: NilaiDb? = null

        fun getInstance(context: Context): NilaiDb {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        NilaiDb::class.java,
                        "ScoreCalc.db"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}