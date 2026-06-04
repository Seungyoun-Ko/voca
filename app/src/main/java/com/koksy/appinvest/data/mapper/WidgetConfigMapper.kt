package com.koksy.appinvest.data.mapper

import com.koksy.appinvest.data.local.entity.WidgetConfigEntity
import com.koksy.appinvest.domain.model.WidgetConfig
import com.koksy.appinvest.domain.model.WidgetPosition
import com.koksy.appinvest.domain.model.WidgetSpan

fun WidgetConfigEntity.toDomain(): WidgetConfig {
    return WidgetConfig(
        id = id,
        type = type,
        title = title,
        position = WidgetPosition(
            x = gridX,
            y = gridY,
            order = gridOrder,
        ),
        span = WidgetSpan(
            columns = columnSpan,
            rows = rowSpan,
        ),
        isPinned = isPinned,
    )
}

fun WidgetConfig.toEntity(updatedAtMillis: Long = System.currentTimeMillis()): WidgetConfigEntity {
    return WidgetConfigEntity(
        id = id,
        type = type,
        title = title,
        gridX = position.x,
        gridY = position.y,
        gridOrder = position.order,
        columnSpan = span.columns,
        rowSpan = span.rows,
        isPinned = isPinned,
        updatedAtMillis = updatedAtMillis,
    )
}
