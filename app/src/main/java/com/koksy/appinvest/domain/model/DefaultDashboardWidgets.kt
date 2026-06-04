package com.koksy.appinvest.domain.model

object DefaultDashboardWidgets {
    fun createDefaults(): List<WidgetConfig> {
        return DashboardLayoutPlanner.normalize(
            listOf(
                create(
                    id = "market-index",
                    type = DashboardWidgetType.MARKET_INDEX,
                    order = 0,
                    isPinned = true,
                ),
                create(
                    id = "mini-heatmap",
                    type = DashboardWidgetType.MINI_HEATMAP,
                    order = 1,
                ),
                create(
                    id = "dividend-calendar",
                    type = DashboardWidgetType.DIVIDEND_CALENDAR,
                    order = 2,
                ),
                create(
                    id = "theme-ranking",
                    type = DashboardWidgetType.THEME_RANKING,
                    order = 3,
                ),
            ),
        )
    }

    fun create(
        id: String,
        type: DashboardWidgetType,
        order: Int,
        isPinned: Boolean = false,
    ): WidgetConfig {
        return WidgetConfig(
            id = id,
            type = type,
            title = type.defaultTitle,
            position = WidgetPosition(
                x = 0,
                y = order,
                order = order,
            ),
            span = WidgetSpan(
                columns = type.defaultColumnSpan,
                rows = type.defaultRowSpan,
            ),
            isPinned = isPinned,
        )
    }
}
