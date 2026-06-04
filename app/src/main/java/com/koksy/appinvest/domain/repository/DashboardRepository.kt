package com.koksy.appinvest.domain.repository

import com.koksy.appinvest.domain.model.WidgetConfig
import kotlinx.coroutines.flow.Flow

interface DashboardRepository {
    fun observeWidgets(): Flow<List<WidgetConfig>>

    suspend fun seedDefaultWidgetsIfEmpty(defaultWidgets: List<WidgetConfig>)

    suspend fun saveWidgetConfig(config: WidgetConfig)

    suspend fun saveWidgetConfigs(configs: List<WidgetConfig>)

    suspend fun deleteWidgetConfig(id: String)

    suspend fun replaceWidgetConfigs(configs: List<WidgetConfig>)
}
