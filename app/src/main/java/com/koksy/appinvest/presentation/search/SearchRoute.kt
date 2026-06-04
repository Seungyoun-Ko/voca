package com.koksy.appinvest.presentation.search

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.koksy.appinvest.AppInvestApplication

@Composable
fun SearchRoute() {
    val appContainer = (LocalContext.current.applicationContext as AppInvestApplication).appContainer
    val viewModel: SearchViewModel = viewModel(
        factory = SearchViewModel.provideFactory(
            searchStocksUseCase = appContainer.searchStocksUseCase,
            observeStockDetailUseCase = appContainer.observeStockDetailUseCase,
            observeMetricComparisonUseCase = appContainer.observeMetricComparisonUseCase,
        ),
    )
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    SearchScreen(
        uiState = uiState.value,
        onQueryChanged = viewModel::onQueryChanged,
        onSymbolSelected = viewModel::selectSymbol,
        onComparisonGroupSelected = viewModel::selectComparisonGroup,
    )
}
