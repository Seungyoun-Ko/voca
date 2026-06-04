package com.koksy.appinvest.presentation.search

import com.koksy.appinvest.domain.model.StockDetail
import com.koksy.appinvest.domain.model.StockSearchResult

data class SearchUiState(
    val query: String = "",
    val selectedSymbol: String = "NVDA",
    val searchResults: List<StockSearchResult> = emptyList(),
    val selectedDetail: StockDetail? = null,
    val isLoading: Boolean = false,
)
