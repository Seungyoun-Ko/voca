package com.koksy.appinvest.presentation.ranking

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.koksy.appinvest.AppInvestApplication

@Composable
fun RankingRoute() {
    val appContainer = (LocalContext.current.applicationContext as AppInvestApplication).appContainer
    val viewModel: RankingViewModel = viewModel(
        factory = RankingViewModel.provideFactory(
            observeStockRankingsUseCase = appContainer.observeStockRankingsUseCase,
            observeThemeRankingsUseCase = appContainer.observeThemeRankingsUseCase,
        ),
    )
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    RankingScreen(
        uiState = uiState.value,
        onCategorySelected = viewModel::selectCategory,
    )
}
