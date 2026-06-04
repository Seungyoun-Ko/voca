package com.koksy.appinvest.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.koksy.appinvest.domain.usecase.ObserveStockDetailUseCase
import com.koksy.appinvest.domain.usecase.SearchStocksUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModel(
    private val searchStocksUseCase: SearchStocksUseCase,
    private val observeStockDetailUseCase: ObserveStockDetailUseCase,
) : ViewModel() {
    private val query = MutableStateFlow("")
    private val selectedSymbol = MutableStateFlow("NVDA")

    private val searchResults = query.flatMapLatest { keyword ->
        searchStocksUseCase(keyword)
    }

    private val selectedDetail = selectedSymbol.flatMapLatest { symbol ->
        observeStockDetailUseCase(symbol)
    }

    val uiState: StateFlow<SearchUiState> = combine(
        query,
        selectedSymbol,
        searchResults,
        selectedDetail,
    ) { keyword, symbol, results, detail ->
        SearchUiState(
            query = keyword,
            selectedSymbol = symbol,
            searchResults = results,
            selectedDetail = detail,
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SearchUiState(isLoading = true),
        )

    fun onQueryChanged(value: String) {
        query.value = value
    }

    fun selectSymbol(symbol: String) {
        selectedSymbol.value = symbol
    }

    companion object {
        fun provideFactory(
            searchStocksUseCase: SearchStocksUseCase,
            observeStockDetailUseCase: ObserveStockDetailUseCase,
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras,
                ): T {
                    return SearchViewModel(
                        searchStocksUseCase = searchStocksUseCase,
                        observeStockDetailUseCase = observeStockDetailUseCase,
                    ) as T
                }
            }
        }
    }
}
