package com.koksy.appinvest.domain.usecase

import com.koksy.appinvest.domain.model.DashboardLayoutPlanner
import com.koksy.appinvest.domain.model.WidgetConfig
import com.koksy.appinvest.domain.repository.DashboardRepository

class DeleteDashboardWidgetUseCase(
    private val dashboardRepository: DashboardRepository,
) {
    suspend operator fun invoke(
        widgetId: String,
        currentWidgets: List<WidgetConfig>,
    ) {
        dashboardRepository.deleteWidgetConfig(widgetId)
        dashboardRepository.saveWidgetConfigs(
            DashboardLayoutPlanner.normalize(currentWidgets.filterNot { it.id == widgetId }),
        )
    }
}
