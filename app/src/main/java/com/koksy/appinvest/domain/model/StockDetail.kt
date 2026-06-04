package com.koksy.appinvest.domain.model

data class StockDetail(
    val symbol: String,
    val name: String,
    val market: String,
    val assetType: AssetType,
    val price: String,
    val changeRate: Double,
    val marketCap: String,
    val tradingValue: String,
    val themeTags: List<String>,
    val chartPoints: List<PricePoint>,
    val financials: List<FinancialMetric>,
    val summary: String,
)

data class PricePoint(
    val label: String,
    val close: Double,
)

data class FinancialMetric(
    val label: String,
    val value: String,
    val helper: String,
)
