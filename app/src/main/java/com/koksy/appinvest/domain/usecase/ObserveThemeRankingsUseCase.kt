package com.koksy.appinvest.domain.usecase

import com.koksy.appinvest.domain.repository.RankingRepository

class ObserveThemeRankingsUseCase(
    private val rankingRepository: RankingRepository,
) {
    operator fun invoke() = rankingRepository.observeThemeRankings()
}
