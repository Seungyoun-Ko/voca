package com.koksy.appinvest.domain.model

data class StockSearchResult(
    val symbol: String,
    val name: String,
    val market: String,
    val assetType: AssetType,
    val themeTags: List<String>,
)

enum class AssetType(val label: String) {
    STOCK("Stock"),
    ETF("ETF"),
}
