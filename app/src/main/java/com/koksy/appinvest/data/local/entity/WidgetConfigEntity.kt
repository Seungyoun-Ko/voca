package com.koksy.appinvest.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.koksy.appinvest.domain.model.DashboardWidgetType

@Entity(tableName = "widget_configs")
data class WidgetConfigEntity(
    @PrimaryKey val id: String,
    val type: DashboardWidgetType,
    val title: String,
    val gridX: Int,
    val gridY: Int,
    val gridOrder: Int,
    val columnSpan: Int,
    val rowSpan: Int,
    val isPinned: Boolean,
    val updatedAtMillis: Long,
)
