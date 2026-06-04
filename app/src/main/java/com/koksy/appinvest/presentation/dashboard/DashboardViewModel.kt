package com.koksy.appinvest.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.koksy.appinvest.domain.model.DashboardWidgetType
import com.koksy.appinvest.domain.usecase.AddDashboardWidgetUseCase
import com.koksy.appinvest.domain.usecase.DeleteDashboardWidgetUseCase
import com.koksy.appinvest.domain.usecase.InitializeDashboardWidgetsUseCase
import com.koksy.appinvest.domain.usecase.MoveDashboardWidgetUseCase
import com.koksy.appinvest.domain.usecase.MoveDirection
import com.koksy.appinvest.domain.usecase.ObserveDashboardWidgetsUseCase
import com.koksy.appinvest.domain.usecase.ResetDashboardWidgetsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val initializeDashboardWidgetsUseCase: InitializeDashboardWidgetsUseCase,
    observeDashboardWidgetsUseCase: ObserveDashboardWidgetsUseCase,
    private val addDashboardWidgetUseCase: AddDashboardWidgetUseCase,
    private val deleteDashboardWidgetUseCase: DeleteDashboardWidgetUseCase,
    private val moveDashboardWidgetUseCase: MoveDashboardWidgetUseCase,
    private val resetDashboardWidgetsUseCase: ResetDashboardWidgetsUseCase,
) : ViewModel() {
    private val isWidgetCatalogOpen = MutableStateFlow(false)

    val uiState: StateFlow<DashboardUiState> = combine(
        observeDashboardWidgetsUseCase(),
        isWidgetCatalogOpen,
    ) { widgets, isCatalogOpen ->
        DashboardUiState(
            widgets = widgets,
            isWidgetCatalogOpen = isCatalogOpen,
        )
    }
        .map { state ->
            state.copy(isLoading = false)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = DashboardUiState(isLoading = true),
        )

    init {
        viewModelScope.launch {
            initializeDashboardWidgetsUseCase()
        }
    }

    fun openWidgetCatalog() {
        isWidgetCatalogOpen.value = true
    }

    fun dismissWidgetCatalog() {
        isWidgetCatalogOpen.value = false
    }

    fun addWidget(type: DashboardWidgetType) {
        viewModelScope.launch {
            addDashboardWidgetUseCase(
                type = type,
                currentWidgets = uiState.value.widgets,
            )
            dismissWidgetCatalog()
        }
    }

    fun deleteWidget(widgetId: String) {
        viewModelScope.launch {
            deleteDashboardWidgetUseCase(
                widgetId = widgetId,
                currentWidgets = uiState.value.widgets,
            )
        }
    }

    fun moveWidgetUp(widgetId: String) {
        moveWidget(widgetId, MoveDirection.UP)
    }

    fun moveWidgetDown(widgetId: String) {
        moveWidget(widgetId, MoveDirection.DOWN)
    }

    fun resetDashboard() {
        viewModelScope.launch {
            resetDashboardWidgetsUseCase()
        }
    }

    private fun moveWidget(
        widgetId: String,
        direction: MoveDirection,
    ) {
        viewModelScope.launch {
            moveDashboardWidgetUseCase(
                widgetId = widgetId,
                direction = direction,
                currentWidgets = uiState.value.widgets,
            )
        }
    }

    companion object {
        fun provideFactory(
            initializeDashboardWidgetsUseCase: InitializeDashboardWidgetsUseCase,
            observeDashboardWidgetsUseCase: ObserveDashboardWidgetsUseCase,
            addDashboardWidgetUseCase: AddDashboardWidgetUseCase,
            deleteDashboardWidgetUseCase: DeleteDashboardWidgetUseCase,
            moveDashboardWidgetUseCase: MoveDashboardWidgetUseCase,
            resetDashboardWidgetsUseCase: ResetDashboardWidgetsUseCase,
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras,
                ): T {
                    return DashboardViewModel(
                        initializeDashboardWidgetsUseCase = initializeDashboardWidgetsUseCase,
                        observeDashboardWidgetsUseCase = observeDashboardWidgetsUseCase,
                        addDashboardWidgetUseCase = addDashboardWidgetUseCase,
                        deleteDashboardWidgetUseCase = deleteDashboardWidgetUseCase,
                        moveDashboardWidgetUseCase = moveDashboardWidgetUseCase,
                        resetDashboardWidgetsUseCase = resetDashboardWidgetsUseCase,
                    ) as T
                }
            }
        }
    }
}
