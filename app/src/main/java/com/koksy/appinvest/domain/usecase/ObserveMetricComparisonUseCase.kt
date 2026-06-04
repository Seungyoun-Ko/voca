package com.koksy.appinvest.domain.usecase

import com.koksy.appinvest.domain.model.ComparisonMetricGroup
import com.koksy.appinvest.domain.repository.SearchRepository

class ObserveMetricComparisonUseCase(
    private val searchRepository: SearchRepository,
) {
    operator fun invoke(
        symbol: String,
        group: ComparisonMetricGroup,
    ) = searchRepository.observeMetricComparison(symbol, group)
}
