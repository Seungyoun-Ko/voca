package com.koksy.appinvest.presentation.ranking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.koksy.appinvest.domain.model.RankingCategory
import com.koksy.appinvest.domain.usecase.ObserveStockRankingsUseCase
import com.koksy.appinvest.domain.usecase.ObserveThemeRankingsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import androidx.lifecycle.viewModelScope

@OptIn(ExperimentalCoroutinesApi::class)
class RankingViewModel(
    private val observeStockRankingsUseCase: ObserveStockRankingsUseCase,
    private val observeThemeRankingsUseCase: ObserveThemeRankingsUseCase,
) : ViewModel() {
    private val selectedCategory = MutableStateFlow(RankingCategory.TRADING_VALUE)

    val uiState: StateFlow<RankingUiState> = selectedCategory
        .flatMapLatest { category ->
            if (category == RankingCategory.THEMES) {
                observeThemeRankingsUseCase().map { themeRankings ->
                    RankingUiState(
                        selectedCategory = category,
                        themeRankings = themeRankings,
                    )
                }
            } else {
                observeStockRankingsUseCase(category).map { stockRankings ->
                    RankingUiState(
                        selectedCategory = category,
                        stockRankings = stockRankings,
                    )
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = RankingUiState(isLoading = true),
        )

    fun selectCategory(category: RankingCategory) {
        selectedCategory.value = category
    }

    companion object {
        fun provideFactory(
            observeStockRankingsUseCase: ObserveStockRankingsUseCase,
            observeThemeRankingsUseCase: ObserveThemeRankingsUseCase,
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras,
                ): T {
                    return RankingViewModel(
                        observeStockRankingsUseCase = observeStockRankingsUseCase,
                        observeThemeRankingsUseCase = observeThemeRankingsUseCase,
                    ) as T
                }
            }
        }
    }
}
