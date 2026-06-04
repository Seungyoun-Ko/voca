package com.koksy.appinvest.di

import android.content.Context
import com.koksy.appinvest.data.local.AppDatabase
import com.koksy.appinvest.data.remote.NetworkClient
import com.koksy.appinvest.data.repository.DummyRankingRepository
import com.koksy.appinvest.data.repository.DummySearchRepository
import com.koksy.appinvest.data.repository.RoomDashboardRepository
import com.koksy.appinvest.domain.repository.DashboardRepository
import com.koksy.appinvest.domain.repository.RankingRepository
import com.koksy.appinvest.domain.repository.SearchRepository
import com.koksy.appinvest.domain.usecase.AddDashboardWidgetUseCase
import com.koksy.appinvest.domain.usecase.DeleteDashboardWidgetUseCase
import com.koksy.appinvest.domain.usecase.InitializeDashboardWidgetsUseCase
import com.koksy.appinvest.domain.usecase.MoveDashboardWidgetUseCase
import com.koksy.appinvest.domain.usecase.ObserveDashboardWidgetsUseCase
import com.koksy.appinvest.domain.usecase.ObserveStockDetailUseCase
import com.koksy.appinvest.domain.usecase.ObserveStockRankingsUseCase
import com.koksy.appinvest.domain.usecase.ObserveThemeRankingsUseCase
import com.koksy.appinvest.domain.usecase.ResetDashboardWidgetsUseCase
import com.koksy.appinvest.domain.usecase.SearchStocksUseCase

class AppContainer(context: Context) {
    val database: AppDatabase = AppDatabase.create(context)
    val networkClient: NetworkClient = NetworkClient

    private val dashboardRepository: DashboardRepository = RoomDashboardRepository(
        widgetConfigDao = database.widgetConfigDao(),
    )
    private val rankingRepository: RankingRepository = DummyRankingRepository()
    private val searchRepository: SearchRepository = DummySearchRepository()

    val initializeDashboardWidgetsUseCase = InitializeDashboardWidgetsUseCase(dashboardRepository)
    val observeDashboardWidgetsUseCase = ObserveDashboardWidgetsUseCase(dashboardRepository)
    val addDashboardWidgetUseCase = AddDashboardWidgetUseCase(dashboardRepository)
    val deleteDashboardWidgetUseCase = DeleteDashboardWidgetUseCase(dashboardRepository)
    val moveDashboardWidgetUseCase = MoveDashboardWidgetUseCase(dashboardRepository)
    val resetDashboardWidgetsUseCase = ResetDashboardWidgetsUseCase(dashboardRepository)

    val observeStockRankingsUseCase = ObserveStockRankingsUseCase(rankingRepository)
    val observeThemeRankingsUseCase = ObserveThemeRankingsUseCase(rankingRepository)

    val searchStocksUseCase = SearchStocksUseCase(searchRepository)
    val observeStockDetailUseCase = ObserveStockDetailUseCase(searchRepository)
}
