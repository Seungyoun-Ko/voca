package com.koksy.appinvest.domain.usecase

import com.koksy.appinvest.domain.repository.SearchRepository

class SearchStocksUseCase(
    private val searchRepository: SearchRepository,
) {
    operator fun invoke(query: String) = searchRepository.searchStocks(query)
}
