package com.koksy.appinvest.domain.repository

import com.koksy.appinvest.domain.model.ComparisonMetricGroup
import com.koksy.appinvest.domain.model.MetricComparisonSet
import com.koksy.appinvest.domain.model.StockDetail
import com.koksy.appinvest.domain.model.StockSearchResult
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun searchStocks(query: String): Flow<List<StockSearchResult>>

    fun observeStockDetail(symbol: String): Flow<StockDetail?>

    fun observeMetricComparison(
        symbol: String,
        group: ComparisonMetricGroup,
    ): Flow<MetricComparisonSet>
}
