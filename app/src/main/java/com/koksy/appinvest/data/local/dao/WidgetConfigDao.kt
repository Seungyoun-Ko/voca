package com.koksy.appinvest.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.koksy.appinvest.data.local.entity.WidgetConfigEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WidgetConfigDao {
    @Query("SELECT * FROM widget_configs ORDER BY gridOrder ASC")
    fun observeWidgetConfigs(): Flow<List<WidgetConfigEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertWidgetConfig(config: WidgetConfigEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertWidgetConfigs(configs: List<WidgetConfigEntity>)

    @Query("SELECT COUNT(*) FROM widget_configs")
    suspend fun getWidgetConfigCount(): Int

    @Query("DELETE FROM widget_configs WHERE id = :id")
    suspend fun deleteWidgetConfig(id: String)

    @Query("DELETE FROM widget_configs")
    suspend fun clearWidgetConfigs()

    @Transaction
    suspend fun replaceWidgetConfigs(configs: List<WidgetConfigEntity>) {
        clearWidgetConfigs()
        upsertWidgetConfigs(configs)
    }
}
