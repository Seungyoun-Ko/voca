package com.koksy.appinvest.presentation.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.koksy.appinvest.AppInvestApplication

@Composable
fun DashboardRoute() {
    val appContainer = (LocalContext.current.applicationContext as AppInvestApplication).appContainer
    val viewModel: DashboardViewModel = viewModel(
        factory = DashboardViewModel.provideFactory(
            initializeDashboardWidgetsUseCase = appContainer.initializeDashboardWidgetsUseCase,
            observeDashboardWidgetsUseCase = appContainer.observeDashboardWidgetsUseCase,
            addDashboardWidgetUseCase = appContainer.addDashboardWidgetUseCase,
            deleteDashboardWidgetUseCase = appContainer.deleteDashboardWidgetUseCase,
            moveDashboardWidgetUseCase = appContainer.moveDashboardWidgetUseCase,
            resetDashboardWidgetsUseCase = appContainer.resetDashboardWidgetsUseCase,
        ),
    )
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    DashboardScreen(
        uiState = uiState.value,
        onAddWidgetClick = viewModel::openWidgetCatalog,
        onDismissWidgetCatalog = viewModel::dismissWidgetCatalog,
        onWidgetTypeSelected = viewModel::addWidget,
        onDeleteWidget = viewModel::deleteWidget,
        onMoveWidgetUp = viewModel::moveWidgetUp,
        onMoveWidgetDown = viewModel::moveWidgetDown,
        onResetDashboard = viewModel::resetDashboard,
    )
}
