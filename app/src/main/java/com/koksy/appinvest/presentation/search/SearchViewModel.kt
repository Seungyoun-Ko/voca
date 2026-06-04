package com.koksy.appinvest.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.koksy.appinvest.domain.model.ComparisonMetricGroup
import com.koksy.appinvest.domain.usecase.ObserveMetricComparisonUseCase
import com.koksy.appinvest.domain.usecase.ObserveStockDetailUseCase
import com.koksy.appinvest.domain.usecase.SearchStocksUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModel(
    private val searchStocksUseCase: SearchStocksUseCase,
    private val observeStockDetailUseCase: ObserveStockDetailUseCase,
    private val observeMetricComparisonUseCase: ObserveMetricComparisonUseCase,
) : ViewModel() {
    private val controls = MutableStateFlow(SearchControls())

    private val searchResults = controls
        .map { it.query }
        .distinctUntilChanged()
        .flatMapLatest { keyword ->
            searchStocksUseCase(keyword)
        }

    private val selectedDetail = controls
        .map { it.selectedSymbol }
        .distinctUntilChanged()
        .flatMapLatest { symbol ->
            observeStockDetailUseCase(symbol)
        }

    private val metricComparison = controls
        .map { it.selectedSymbol to it.selectedComparisonGroup }
        .distinctUntilChanged()
        .flatMapLatest { (symbol, group) ->
            observeMetricComparisonUseCase(symbol, group)
        }

    val uiState: StateFlow<SearchUiState> = combine(
        controls,
        searchResults,
        selectedDetail,
        metricComparison,
    ) { controls, results, detail, comparison ->
        SearchUiState(
            query = controls.query,
            selectedSymbol = controls.selectedSymbol,
            selectedComparisonGroup = controls.selectedComparisonGroup,
            searchResults = results,
            selectedDetail = detail,
            metricComparison = comparison,
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SearchUiState(isLoading = true),
        )

    fun onQueryChanged(value: String) {
        controls.update { current ->
            current.copy(query = value)
        }
    }

    fun selectSymbol(symbol: String) {
        controls.update { current ->
            current.copy(selectedSymbol = symbol)
        }
    }

    fun selectComparisonGroup(group: ComparisonMetricGroup) {
        controls.update { current ->
            current.copy(selectedComparisonGroup = group)
        }
    }

    companion object {
        fun provideFactory(
            searchStocksUseCase: SearchStocksUseCase,
            observeStockDetailUseCase: ObserveStockDetailUseCase,
            observeMetricComparisonUseCase: ObserveMetricComparisonUseCase,
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
                        observeMetricComparisonUseCase = observeMetricComparisonUseCase,
                    ) as T
                }
            }
        }
    }

    private data class SearchControls(
        val query: String = "",
        val selectedSymbol: String = "NVDA",
        val selectedComparisonGroup: ComparisonMetricGroup = ComparisonMetricGroup.BASIC,
    )
}
