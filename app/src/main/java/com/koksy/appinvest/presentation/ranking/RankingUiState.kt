package com.koksy.appinvest.presentation.ranking

import com.koksy.appinvest.domain.model.RankingCategory
import com.koksy.appinvest.domain.model.StockRankingItem
import com.koksy.appinvest.domain.model.ThemeRankingItem

data class RankingUiState(
    val selectedCategory: RankingCategory = RankingCategory.TRADING_VALUE,
    val categories: List<RankingCategory> = RankingCategory.entries,
    val stockRankings: List<StockRankingItem> = emptyList(),
    val themeRankings: List<ThemeRankingItem> = emptyList(),
    val isLoading: Boolean = false,
)
