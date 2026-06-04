package com.koksy.appinvest.presentation.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.koksy.appinvest.R
import com.koksy.appinvest.domain.model.DashboardWidgetType
import com.koksy.appinvest.domain.model.WidgetConfig

private const val DashboardColumns = 2

@Composable
fun DashboardScreen(
    uiState: DashboardUiState,
    onAddWidgetClick: () -> Unit,
    onDismissWidgetCatalog: () -> Unit,
    onWidgetTypeSelected: (DashboardWidgetType) -> Unit,
    onDeleteWidget: (String) -> Unit,
    onMoveWidgetUp: (String) -> Unit,
    onMoveWidgetDown: (String) -> Unit,
    onResetDashboard: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        if (uiState.isWidgetCatalogOpen) {
            WidgetCatalogDialog(
                widgetTypes = uiState.widgetCatalog,
                onDismiss = onDismissWidgetCatalog,
                onWidgetTypeSelected = onWidgetTypeSelected,
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(DashboardColumns),
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                top = 18.dp,
                bottom = 18.dp,
            ),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                DashboardHeader(
                    onAddWidgetClick = onAddWidgetClick,
                    onResetDashboard = onResetDashboard,
                )
            }

            if (uiState.isLoading) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    LoadingDashboard()
                }
            }

            itemsIndexed(
                items = uiState.widgets,
                key = { _, config -> config.id },
                span = { _, config ->
                    GridItemSpan(config.span.columns.coerceIn(1, maxLineSpan))
                },
            ) { index, config ->
                DashboardWidgetCard(
                    config = config,
                    canMoveUp = index > 0,
                    canMoveDown = index < uiState.widgets.lastIndex,
                    onMoveUp = { onMoveWidgetUp(config.id) },
                    onMoveDown = { onMoveWidgetDown(config.id) },
                    onDelete = { onDeleteWidget(config.id) },
                )
            }
        }
    }
}

@Composable
private fun DashboardHeader(
    onAddWidgetClick: () -> Unit,
    onResetDashboard: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = "Dashboard",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = "오늘의 시장",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedButton(onClick = onResetDashboard) {
                Icon(
                    painter = painterResource(R.drawable.ic_restart),
                    contentDescription = null,
                )
                Text(
                    text = "초기화",
                    modifier = Modifier.padding(start = 6.dp),
                )
            }
            Button(onClick = onAddWidgetClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_add),
                    contentDescription = null,
                )
                Text(
                    text = "추가",
                    modifier = Modifier.padding(start = 6.dp),
                )
            }
        }
    }
}

@Composable
private fun DashboardWidgetCard(
    config: WidgetConfig,
    canMoveUp: Boolean,
    canMoveDown: Boolean,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height((176 * config.span.rows.coerceAtLeast(1)).dp)
            .pointerInput(config.id) {
                // TODO: Connect detectDragGestures here after grid collision and
                // persistence rules are defined for widget reorder/resize.
            },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column {
                    Text(
                        text = config.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                    )
                    Text(
                        text = config.type.label,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (config.isPinned) {
                    Text(
                        text = "Pinned",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                    )
                } else {
                    Box(modifier = Modifier.width(1.dp))
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(
                        onClick = onMoveUp,
                        enabled = canMoveUp,
                        modifier = Modifier.size(32.dp),
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_up),
                            contentDescription = "Move up",
                        )
                    }
                    IconButton(
                        onClick = onMoveDown,
                        enabled = canMoveDown,
                        modifier = Modifier.size(32.dp),
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_down),
                            contentDescription = "Move down",
                        )
                    }
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(32.dp),
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_delete),
                            contentDescription = "Delete widget",
                            tint = MaterialTheme.colorScheme.error,
                        )
                    }
                }
            }

            WidgetContent(config = config)
        }
    }
}

@Composable
private fun WidgetContent(config: WidgetConfig) {
    when (config.type) {
        DashboardWidgetType.MARKET_INDEX -> MarketIndexContent()
        DashboardWidgetType.MINI_HEATMAP -> MiniHeatmapContent()
        DashboardWidgetType.DIVIDEND_CALENDAR -> DividendCalendarContent()
        DashboardWidgetType.THEME_RANKING -> ThemeRankingContent()
    }
}

@Composable
private fun WidgetCatalogDialog(
    widgetTypes: List<DashboardWidgetType>,
    onDismiss: () -> Unit,
    onWidgetTypeSelected: (DashboardWidgetType) -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "위젯 추가")
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                widgetTypes.forEach { type ->
                    TextButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onWidgetTypeSelected(type) },
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(text = type.defaultTitle)
                            Text(
                                text = "${type.defaultColumnSpan}x${type.defaultRowSpan}",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "닫기")
            }
        },
    )
}

@Composable
private fun LoadingDashboard() {
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

@Composable
private fun MarketIndexContent() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        IndexBadge(
            name = "KOSPI",
            value = "2,742.11",
            change = "+1.42%",
            modifier = Modifier.weight(1f),
        )
        IndexBadge(
            name = "NASDAQ",
            value = "17,978.02",
            change = "-0.18%",
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun IndexBadge(
    name: String,
    value: String,
    change: String,
    modifier: Modifier = Modifier,
) {
    val changeColor = if (change.startsWith("+")) Color(0xFF0F8B5F) else Color(0xFFC24135)

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
            text = name,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )
        Text(
            text = change,
            style = MaterialTheme.typography.labelMedium,
            color = changeColor,
        )
    }
}

@Composable
private fun MiniHeatmapContent() {
    val colors = listOf(
        Color(0xFF0F8B5F),
        Color(0xFF58A55C),
        Color(0xFFC24135),
        Color(0xFF2F6F6D),
        Color(0xFFE6A23C),
        Color(0xFFB64335),
        Color(0xFF6BAA75),
        Color(0xFF134E4A),
        Color(0xFFD97706),
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        colors.chunked(3).forEach { rowColors ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                rowColors.forEach { color ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(24.dp)
                            .background(color = color, shape = RoundedCornerShape(6.dp)),
                    )
                }
            }
        }
    }
}

@Composable
private fun DividendCalendarContent() {
    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        DividendRow(date = "06/12", ticker = "AAPL", amount = "$0.26")
        DividendRow(date = "06/18", ticker = "SCHD", amount = "$0.61")
    }
}

@Composable
private fun DividendRow(
    date: String,
    ticker: String,
    amount: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = "$date  $ticker",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = amount,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ThemeRankingContent() {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        ThemeChip(label = "SMR", rank = "1")
        ThemeChip(label = "Data Center", rank = "2")
        ThemeChip(label = "Quantum", rank = "3")
    }
}

@Composable
private fun ThemeChip(
    label: String,
    rank: String,
) {
    Row(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(8.dp),
            )
            .padding(horizontal = 10.dp, vertical = 7.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = rank,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            fontWeight = FontWeight.SemiBold,
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        )
    }
}
