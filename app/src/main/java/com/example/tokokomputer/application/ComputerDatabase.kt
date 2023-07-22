package com.example.tokokomputer.application

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.tokokomputer.dao.ComputerDao
import com.example.tokokomputer.model.Computer

@Database(entities = [Computer::class], version = 2, exportSchema = false)
abstract class ComputerDatabase:RoomDatabase() {
    abstract fun computerDao(): ComputerDao

    companion object {
        private var INSTANCE: ComputerDatabase? = null

        //miggrasi database versi 1 ke 2 karena adaperubahan  table

        private val migration1To2: Migration = object: Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE computer_table ADD COLUMN latitude Double DEFAULT 0.0")
                database.execSQL("ALTER TABLE computer_table ADD COLUMN longitude Double DEFAULT 0.0")

            }

        }

        fun getDatabase(context: Context): ComputerDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ComputerDatabase::class.java,
                    "computer_database"
                )
                    .addMigrations(migration1To2)
                    .allowMainThreadQueries()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}