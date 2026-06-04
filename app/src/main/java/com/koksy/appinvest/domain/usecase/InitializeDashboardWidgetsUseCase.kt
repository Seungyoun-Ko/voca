package com.koksy.appinvest.domain.usecase

import com.koksy.appinvest.domain.model.DefaultDashboardWidgets
import com.koksy.appinvest.domain.repository.DashboardRepository

class InitializeDashboardWidgetsUseCase(
    private val dashboardRepository: DashboardRepository,
) {
    suspend operator fun invoke() {
        dashboardRepository.seedDefaultWidgetsIfEmpty(DefaultDashboardWidgets.createDefaults())
    }
}
