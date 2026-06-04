package com.koksy.appinvest.domain.usecase

import com.koksy.appinvest.domain.model.RankingCategory
import com.koksy.appinvest.domain.repository.RankingRepository

class ObserveStockRankingsUseCase(
    private val rankingRepository: RankingRepository,
) {
    operator fun invoke(category: RankingCategory) = rankingRepository.observeStockRankings(category)
}
