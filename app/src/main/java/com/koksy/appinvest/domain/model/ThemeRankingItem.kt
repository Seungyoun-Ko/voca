package com.koksy.appinvest.domain.model

data class ThemeRankingItem(
    val rank: Int,
    val name: String,
    val momentumScore: Int,
    val changeRate: Double,
    val leadingStocks: List<String>,
    val summary: String,
)
