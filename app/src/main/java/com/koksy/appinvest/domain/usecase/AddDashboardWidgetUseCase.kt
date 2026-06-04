package com.koksy.appinvest.domain.usecase

import com.koksy.appinvest.domain.model.DashboardLayoutPlanner
import com.koksy.appinvest.domain.model.DashboardWidgetType
import com.koksy.appinvest.domain.model.DefaultDashboardWidgets
import com.koksy.appinvest.domain.model.WidgetConfig
import com.koksy.appinvest.domain.repository.DashboardRepository

class AddDashboardWidgetUseCase(
    private val dashboardRepository: DashboardRepository,
) {
    suspend operator fun invoke(
        type: DashboardWidgetType,
        currentWidgets: List<WidgetConfig>,
    ) {
        val nextOrder = currentWidgets.maxOfOrNull { it.position.order }?.plus(1) ?: 0
        val newWidget = DefaultDashboardWidgets.create(
            id = "${type.name.lowercase()}-${System.currentTimeMillis()}",
            type = type,
            order = nextOrder,
        )
        dashboardRepository.saveWidgetConfigs(
            DashboardLayoutPlanner.normalize(currentWidgets + newWidget),
        )
    }
}
