package com.koksy.appinvest.domain.model

data class StockRankingItem(
    val rank: Int,
    val symbol: String,
    val name: String,
    val market: String,
    val price: String,
    val changeRate: Double,
    val tradingValue: String,
    val themeTags: List<String>,
)
