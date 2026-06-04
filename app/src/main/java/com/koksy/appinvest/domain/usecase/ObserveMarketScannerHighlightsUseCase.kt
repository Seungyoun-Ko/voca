package com.koksy.appinvest.domain.usecase

import com.koksy.appinvest.domain.repository.RankingRepository

class ObserveMarketScannerHighlightsUseCase(
    private val rankingRepository: RankingRepository,
) {
    operator fun invoke() = rankingRepository.observeMarketScannerHighlights()
}
