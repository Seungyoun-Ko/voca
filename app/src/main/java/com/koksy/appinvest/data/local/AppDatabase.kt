package com.koksy.appinvest.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.koksy.appinvest.data.local.converter.DashboardWidgetTypeConverter
import com.koksy.appinvest.data.local.dao.WidgetConfigDao
import com.koksy.appinvest.data.local.entity.WidgetConfigEntity

@Database(
    entities = [WidgetConfigEntity::class],
    version = 1,
    exportSchema = false,
)
@TypeConverters(DashboardWidgetTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun widgetConfigDao(): WidgetConfigDao

    companion object {
        fun create(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "app_invest.db",
            ).build()
        }
    }
}
