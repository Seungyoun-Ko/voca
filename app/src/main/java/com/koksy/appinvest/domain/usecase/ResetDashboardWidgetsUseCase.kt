package com.koksy.appinvest.domain.usecase

import com.koksy.appinvest.domain.model.DefaultDashboardWidgets
import com.koksy.appinvest.domain.repository.DashboardRepository

class ResetDashboardWidgetsUseCase(
    private val dashboardRepository: DashboardRepository,
) {
    suspend operator fun invoke() {
        dashboardRepository.replaceWidgetConfigs(DefaultDashboardWidgets.createDefaults())
    }
}
