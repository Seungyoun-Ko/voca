package com.koksy.appinvest.presentation.search

import com.koksy.appinvest.domain.model.ComparisonMetricGroup
import com.koksy.appinvest.domain.model.MetricComparisonSet
import com.koksy.appinvest.domain.model.StockDetail
import com.koksy.appinvest.domain.model.StockSearchResult

data class SearchUiState(
    val query: String = "",
    val selectedSymbol: String = "NVDA",
    val selectedComparisonGroup: ComparisonMetricGroup = ComparisonMetricGroup.BASIC,
    val comparisonGroups: List<ComparisonMetricGroup> = ComparisonMetricGroup.values().toList(),
    val searchResults: List<StockSearchResult> = emptyList(),
    val selectedDetail: StockDetail? = null,
    val metricComparison: MetricComparisonSet? = null,
    val isLoading: Boolean = false,
)
