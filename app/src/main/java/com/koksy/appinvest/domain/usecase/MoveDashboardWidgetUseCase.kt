package com.koksy.appinvest.domain.usecase

import com.koksy.appinvest.domain.model.DashboardLayoutPlanner
import com.koksy.appinvest.domain.model.WidgetConfig
import com.koksy.appinvest.domain.repository.DashboardRepository

class MoveDashboardWidgetUseCase(
    private val dashboardRepository: DashboardRepository,
) {
    suspend operator fun invoke(
        widgetId: String,
        direction: MoveDirection,
        currentWidgets: List<WidgetConfig>,
    ) {
        val reorderedWidgets = currentWidgets
            .sortedBy { it.position.order }
            .toMutableList()
        val currentIndex = reorderedWidgets.indexOfFirst { it.id == widgetId }
        if (currentIndex == -1) return

        val targetIndex = when (direction) {
            MoveDirection.UP -> currentIndex - 1
            MoveDirection.DOWN -> currentIndex + 1
        }
        if (targetIndex !in reorderedWidgets.indices) return

        val widget = reorderedWidgets.removeAt(currentIndex)
        reorderedWidgets.add(targetIndex, widget)

        dashboardRepository.saveWidgetConfigs(
            DashboardLayoutPlanner.normalize(reorderedWidgets),
        )
    }
}

enum class MoveDirection {
    UP,
    DOWN,
}
