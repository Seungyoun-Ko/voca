package com.koksy.appinvest.presentation.dashboard

import com.koksy.appinvest.domain.model.DashboardWidgetType
import com.koksy.appinvest.domain.model.WidgetConfig

data class DashboardUiState(
    val widgets: List<WidgetConfig> = emptyList(),
    val isLoading: Boolean = false,
    val isWidgetCatalogOpen: Boolean = false,
    val widgetCatalog: List<DashboardWidgetType> = DashboardWidgetType.entries,
)
