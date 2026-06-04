package com.koksy.appinvest.presentation.search

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.koksy.appinvest.R
import com.koksy.appinvest.domain.model.ComparisonMetricGroup
import com.koksy.appinvest.domain.model.FinancialMetric
import com.koksy.appinvest.domain.model.MetricComparisonRow
import com.koksy.appinvest.domain.model.MetricComparisonSet
import com.koksy.appinvest.domain.model.MetricComparisonValue
import com.koksy.appinvest.domain.model.PricePoint
import com.koksy.appinvest.domain.model.StockDetail
import com.koksy.appinvest.domain.model.StockSearchResult
import java.util.Locale
import kotlin.math.absoluteValue

@Composable
fun SearchScreen(
    uiState: SearchUiState,
    onQueryChanged: (String) -> Unit,
    onSymbolSelected: (String) -> Unit,
    onComparisonGroupSelected: (ComparisonMetricGroup) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                top = 18.dp,
                bottom = 18.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                SearchHeader(
                    query = uiState.query,
                    onQueryChanged = onQueryChanged,
                )
            }

            if (uiState.isLoading) {
                item {
                    LoadingSearch()
                }
            }

            uiState.selectedDetail?.let { detail ->
                item {
                    StockDetailPanel(detail = detail)
                }
            }

            uiState.metricComparison?.let { comparison ->
                item {
                    MetricComparisonPanel(
                        selectedSymbol = uiState.selectedSymbol,
                        selectedGroup = uiState.selectedComparisonGroup,
                        groups = uiState.comparisonGroups,
                        comparison = comparison,
                        onGroupSelected = onComparisonGroupSelected,
                    )
                }
            }

            item {
                SectionTitle(
                    title = "검색 결과",
                    subtitle = "${uiState.searchResults.size}개 종목",
                )
            }

            items(
                items = uiState.searchResults,
                key = { item -> item.symbol },
            ) { item ->
                SearchResultRow(
                    item = item,
                    isSelected = item.symbol == uiState.selectedSymbol,
                    onClick = { onSymbolSelected(item.symbol) },
                )
            }
        }
    }
}

@Composable
private fun SearchHeader(
    query: String,
    onQueryChanged: (String) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = "Search",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = "종목, ETF, 테마 키워드 검색",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        OutlinedTextField(
            value = query,
            onValueChange = onQueryChanged,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            label = { Text(text = "검색어") },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.ic_search),
                    contentDescription = null,
                )
            },
            placeholder = {
                Text(text = "예: NVDA, SMR, 배당")
            },
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun MetricComparisonPanel(
    selectedSymbol: String,
    selectedGroup: ComparisonMetricGroup,
    groups: List<ComparisonMetricGroup>,
    comparison: MetricComparisonSet,
    onGroupSelected: (ComparisonMetricGroup) -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(3.dp),
                ) {
                    Text(
                        text = "동종 기업 비교",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = comparison.peerGroupLabel,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Text(
                    text = selectedGroup.label,
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            shape = RoundedCornerShape(8.dp),
                        )
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                )
            }

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                groups.forEach { group ->
                    FilterChip(
                        selected = group == selectedGroup,
                        onClick = { onGroupSelected(group) },
                        label = {
                            Text(
                                text = group.label,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        },
                    )
                }
            }

            Text(
                text = selectedGroup.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                comparison.rows.forEach { row ->
                    MetricComparisonRowBlock(
                        row = row,
                        selectedSymbol = selectedSymbol,
                    )
                }
            }
        }
    }
}

@Composable
private fun MetricComparisonRowBlock(
    row: MetricComparisonRow,
    selectedSymbol: String,
) {
    val numericValues = row.values.mapNotNull { it.numericValue }
    val bestValue = when {
        numericValues.isEmpty() -> null
        row.higherIsBetter -> numericValues.maxOrNull()
        else -> numericValues.minOrNull()
    }
    val directionText = if (row.higherIsBetter) "높을수록 유리" else "낮을수록 유리"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(8.dp),
            )
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Text(
                    text = row.metricName,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = row.helper,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Text(
                text = directionText,
                modifier = Modifier.padding(start = 8.dp),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.End,
            )
        }

        row.values.forEach { value ->
            val isSelected = value.symbol == selectedSymbol
            val isBest = bestValue != null &&
                value.numericValue != null &&
                (value.numericValue - bestValue).absoluteValue < 0.0001
            ComparisonValueBar(
                value = value,
                fraction = normalizedFraction(value.numericValue, numericValues),
                isSelected = isSelected,
                isBest = isBest,
            )
        }
    }
}

@Composable
private fun ComparisonValueBar(
    value: MetricComparisonValue,
    fraction: Float,
    isSelected: Boolean,
    isBest: Boolean,
) {
    val barColor = when {
        isSelected -> MaterialTheme.colorScheme.primary
        isBest -> MaterialTheme.colorScheme.tertiary
        else -> MaterialTheme.colorScheme.secondary
    }
    val textColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.width(88.dp),
            verticalArrangement = Arrangement.spacedBy(1.dp),
        ) {
            Text(
                text = value.symbol,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                color = textColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = value.displayName,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .height(18.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surface),
        ) {
            if (fraction > 0f) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(fraction)
                        .background(barColor.copy(alpha = if (isSelected) 0.72f else 0.42f)),
                )
            }
        }

        Row(
            modifier = Modifier.widthIn(min = 78.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (isBest) {
                Text(
                    text = "우수",
                    modifier = Modifier.padding(end = 4.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.tertiary,
                    fontWeight = FontWeight.SemiBold,
                )
            }
            Text(
                text = value.displayValue,
                style = MaterialTheme.typography.labelMedium,
                color = if (isSelected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                textAlign = TextAlign.End,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

private fun normalizedFraction(
    value: Double?,
    values: List<Double>,
): Float {
    if (value == null || values.isEmpty()) return 0f
    val min = values.minOrNull() ?: return 0f
    val max = values.maxOrNull() ?: return 0f
    if ((max - min).absoluteValue < 0.0001) return 1f

    return ((value - min) / (max - min))
        .toFloat()
        .coerceIn(0.08f, 1f)
}

@Composable
private fun StockDetailPanel(detail: StockDetail) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(3.dp),
                ) {
                    Text(
                        text = detail.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = "${detail.symbol} · ${detail.market} · ${detail.assetType.label}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(3.dp),
                ) {
                    Text(
                        text = detail.price,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                    ChangeRateText(changeRate = detail.changeRate)
                }
            }

            Text(
                text = detail.summary,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            PriceSparkline(
                points = detail.chartPoints,
                changeRate = detail.changeRate,
            )

            DetailFacts(detail = detail)

            MetricGrid(metrics = detail.financials)
        }
    }
}

@Composable
private fun DetailFacts(detail: StockDetail) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        FactTile(
            label = "시가총액",
            value = detail.marketCap,
            modifier = Modifier.weight(1f),
        )
        FactTile(
            label = "거래대금",
            value = detail.tradingValue,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun FactTile(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(8.dp),
            )
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun MetricGrid(metrics: List<FinancialMetric>) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        metrics.forEach { metric ->
            MetricTile(metric = metric)
        }
    }
}

@Composable
private fun MetricTile(metric: FinancialMetric) {
    Column(
        modifier = Modifier
            .width(150.dp)
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(8.dp),
            )
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Text(
            text = metric.label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        )
        Text(
            text = metric.value,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        )
        Text(
            text = metric.helper,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun PriceSparkline(
    points: List<PricePoint>,
    changeRate: Double,
) {
    val lineColor = if (changeRate >= 0) Color(0xFF0F8B5F) else Color(0xFFC24135)
    val gridColor = MaterialTheme.colorScheme.surfaceVariant

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(108.dp)
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(8.dp),
            )
            .padding(10.dp),
    ) {
        if (points.isEmpty()) return@Canvas

        val minValue = points.minOf { it.close }
        val maxValue = points.maxOf { it.close }
        val valueRange = (maxValue - minValue).takeIf { it != 0.0 } ?: 1.0
        val xStep = if (points.size > 1) size.width / (points.size - 1) else size.width

        drawLine(
            color = gridColor,
            start = Offset(0f, size.height),
            end = Offset(size.width, size.height),
            strokeWidth = 2f,
        )

        for (index in 0 until points.lastIndex) {
            val start = points[index]
            val end = points[index + 1]
            drawLine(
                color = lineColor,
                start = Offset(
                    x = xStep * index,
                    y = size.height - (((start.close - minValue) / valueRange).toFloat() * size.height),
                ),
                end = Offset(
                    x = xStep * (index + 1),
                    y = size.height - (((end.close - minValue) / valueRange).toFloat() * size.height),
                ),
                strokeWidth = 5f,
                cap = StrokeCap.Round,
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SearchResultRow(
    item: StockSearchResult,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = if (isSelected) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surface
        },
        tonalElevation = 1.dp,
        onClick = onClick,
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = "${item.symbol} · ${item.market}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Text(
                    text = item.assetType.label,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                item.themeTags.forEach { tag ->
                    AssistChip(
                        onClick = onClick,
                        label = {
                            Text(
                                text = tag,
                                style = MaterialTheme.typography.labelSmall,
                            )
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(
    title: String,
    subtitle: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun ChangeRateText(changeRate: Double) {
    val color = if (changeRate >= 0) Color(0xFF0F8B5F) else Color(0xFFC24135)
    val sign = if (changeRate >= 0) "+" else "-"
    Text(
        text = "$sign${String.format(Locale.US, "%.2f", changeRate.absoluteValue)}%",
        style = MaterialTheme.typography.labelLarge,
        color = color,
        fontWeight = FontWeight.SemiBold,
    )
}

@Composable
private fun LoadingSearch() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .width(24.dp)
                .height(24.dp),
            strokeWidth = 2.dp,
        )
    }
}
