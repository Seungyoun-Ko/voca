package com.koksy.appinvest.data.local.converter

import androidx.room.TypeConverter
import com.koksy.appinvest.domain.model.DashboardWidgetType

class DashboardWidgetTypeConverter {
    @TypeConverter
    fun fromType(type: DashboardWidgetType): String = type.name

    @TypeConverter
    fun toType(value: String): DashboardWidgetType = DashboardWidgetType.valueOf(value)
}
