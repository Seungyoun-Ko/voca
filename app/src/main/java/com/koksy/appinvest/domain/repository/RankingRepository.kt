package com.koksy.appinvest.domain.repository

import com.koksy.appinvest.domain.model.RankingCategory
import com.koksy.appinvest.domain.model.StockRankingItem
import com.koksy.appinvest.domain.model.ThemeRankingItem
import kotlinx.coroutines.flow.Flow

interface RankingRepository {
    fun observeStockRankings(category: RankingCategory): Flow<List<StockRankingItem>>

    fun observeThemeRankings(): Flow<List<ThemeRankingItem>>
}
