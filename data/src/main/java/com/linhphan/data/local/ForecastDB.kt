package com.linhphan.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TblForecast::class], version = 1, exportSchema = false)
abstract class ForecastDB : RoomDatabase() {

    abstract fun getForecastDao(): ForecastDao

    companion object {

        private const val DATABASE_NAME = "WeatherForecast"

        fun build(context: Context): ForecastDB {
            return Room.databaseBuilder(context, ForecastDB::class.java, DATABASE_NAME)
                .build()
        }
    }
}