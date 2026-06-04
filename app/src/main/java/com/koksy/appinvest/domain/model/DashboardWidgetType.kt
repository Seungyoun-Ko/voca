package com.koksy.appinvest.domain.model

enum class DashboardWidgetType(
    val label: String,
    val defaultTitle: String,
    val defaultColumnSpan: Int,
    val defaultRowSpan: Int,
) {
    MARKET_INDEX(
        label = "Market Index",
        defaultTitle = "Major Indices",
        defaultColumnSpan = 2,
        defaultRowSpan = 1,
    ),
    MINI_HEATMAP(
        label = "Mini Heatmap",
        defaultTitle = "Mini Heatmap",
        defaultColumnSpan = 1,
        defaultRowSpan = 2,
    ),
    DIVIDEND_CALENDAR(
        label = "Dividend Calendar",
        defaultTitle = "Dividend Calendar",
        defaultColumnSpan = 1,
        defaultRowSpan = 1,
    ),
    THEME_RANKING(
        label = "Theme Ranking",
        defaultTitle = "Theme Ranking",
        defaultColumnSpan = 2,
        defaultRowSpan = 1,
    ),
}
