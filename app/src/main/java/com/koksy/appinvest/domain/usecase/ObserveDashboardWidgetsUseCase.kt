package com.koksy.appinvest.domain.usecase

import com.koksy.appinvest.domain.repository.DashboardRepository

class ObserveDashboardWidgetsUseCase(
    private val dashboardRepository: DashboardRepository,
) {
    operator fun invoke() = dashboardRepository.observeWidgets()
}
