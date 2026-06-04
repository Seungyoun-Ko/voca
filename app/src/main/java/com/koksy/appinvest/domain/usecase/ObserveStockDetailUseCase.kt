package com.koksy.appinvest.domain.usecase

import com.koksy.appinvest.domain.repository.SearchRepository

class ObserveStockDetailUseCase(
    private val searchRepository: SearchRepository,
) {
    operator fun invoke(symbol: String) = searchRepository.observeStockDetail(symbol)
}
