package com.koksy.appinvest.domain.model

data class WidgetConfig(
    val id: String,
    val type: DashboardWidgetType,
    val title: String,
    val position: WidgetPosition,
    val span: WidgetSpan,
    val isPinned: Boolean = false,
)

data class WidgetPosition(
    val x: Int,
    val y: Int,
    val order: Int,
)

data class WidgetSpan(
    val columns: Int,
    val rows: Int,
)
