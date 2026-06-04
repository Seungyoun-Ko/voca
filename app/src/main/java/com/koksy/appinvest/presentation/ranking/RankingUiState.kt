package com.koksy.appinvest.presentation.ranking

import com.koksy.appinvest.domain.model.MarketScannerHighlight
import com.koksy.appinvest.domain.model.RankingCategory
import com.koksy.appinvest.domain.model.StockRankingItem
import com.koksy.appinvest.domain.model.ThemeRankingItem

data class RankingUiState(
    val selectedCategory: RankingCategory = RankingCategory.UPSIDE,
    val categories: List<RankingCategory> = RankingCategory.entries,
    val scannerHighlights: List<MarketScannerHighlight> = emptyList(),
    val stockRankings: List<StockRankingItem> = emptyList(),
    val themeRankings: List<ThemeRankingItem> = emptyList(),
    val isLoading: Boolean = false,
)
