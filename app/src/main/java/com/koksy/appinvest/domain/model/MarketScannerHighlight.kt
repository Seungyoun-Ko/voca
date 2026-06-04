package com.koksy.appinvest.domain.model

data class MarketScannerHighlight(
    val category: RankingCategory,
    val symbol: String,
    val name: String,
    val market: String,
    val metricLabel: String,
    val metricValue: String,
    val changeRate: Double,
)
