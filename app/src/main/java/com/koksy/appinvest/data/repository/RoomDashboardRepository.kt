package com.koksy.appinvest.data.repository

import com.koksy.appinvest.data.local.dao.WidgetConfigDao
import com.koksy.appinvest.data.mapper.toDomain
import com.koksy.appinvest.data.mapper.toEntity
import com.koksy.appinvest.domain.model.WidgetConfig
import com.koksy.appinvest.domain.repository.DashboardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomDashboardRepository(
    private val widgetConfigDao: WidgetConfigDao,
) : DashboardRepository {
    override fun observeWidgets(): Flow<List<WidgetConfig>> {
        return widgetConfigDao.observeWidgetConfigs()
            .map { entities -> entities.map { it.toDomain() } }
    }

    override suspend fun seedDefaultWidgetsIfEmpty(defaultWidgets: List<WidgetConfig>) {
        if (widgetConfigDao.getWidgetConfigCount() == 0) {
            widgetConfigDao.upsertWidgetConfigs(defaultWidgets.map { it.toEntity() })
        }
    }

    override suspend fun saveWidgetConfig(config: WidgetConfig) {
        widgetConfigDao.upsertWidgetConfig(config.toEntity())
    }

    override suspend fun saveWidgetConfigs(configs: List<WidgetConfig>) {
        widgetConfigDao.upsertWidgetConfigs(configs.map { it.toEntity() })
    }

    override suspend fun deleteWidgetConfig(id: String) {
        widgetConfigDao.deleteWidgetConfig(id)
    }

    override suspend fun replaceWidgetConfigs(configs: List<WidgetConfig>) {
        widgetConfigDao.replaceWidgetConfigs(configs.map { it.toEntity() })
    }
}
