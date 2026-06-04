package com.koksy.appinvest.domain.model

object DashboardLayoutPlanner {
    private const val MaxColumns = 2

    fun normalize(widgets: List<WidgetConfig>): List<WidgetConfig> {
        var row = 0
        var column = 0
        var currentRowHeight = 1

        return widgets
            .sortedBy { it.position.order }
            .mapIndexed { index, widget ->
                val columnSpan = widget.span.columns.coerceIn(1, MaxColumns)
                val rowSpan = widget.span.rows.coerceAtLeast(1)

                if (columnSpan == MaxColumns || column + columnSpan > MaxColumns) {
                    if (column != 0) {
                        row += currentRowHeight
                    }
                    column = 0
                    currentRowHeight = 1
                }

                val placedWidget = widget.copy(
                    position = WidgetPosition(
                        x = column,
                        y = row,
                        order = index,
                    ),
                    span = WidgetSpan(
                        columns = columnSpan,
                        rows = rowSpan,
                    ),
                )

                if (columnSpan == MaxColumns) {
                    row += rowSpan
                    column = 0
                    currentRowHeight = 1
                } else {
                    column += columnSpan
                    currentRowHeight = maxOf(currentRowHeight, rowSpan)
                    if (column >= MaxColumns) {
                        row += currentRowHeight
                        column = 0
                        currentRowHeight = 1
                    }
                }

                placedWidget
            }
    }
}
