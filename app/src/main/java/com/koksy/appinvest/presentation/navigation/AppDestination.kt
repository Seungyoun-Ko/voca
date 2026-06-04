package com.koksy.appinvest.presentation.navigation

import androidx.annotation.DrawableRes
import com.koksy.appinvest.R

enum class AppDestination(
    val route: String,
    val label: String,
    @param:DrawableRes val iconResId: Int,
) {
    Dashboard(
        route = "dashboard",
        label = "Dashboard",
        iconResId = R.drawable.ic_dashboard,
    ),
    Ranking(
        route = "ranking",
        label = "Ranking",
        iconResId = R.drawable.ic_ranking,
    ),
    Search(
        route = "search",
        label = "Search",
        iconResId = R.drawable.ic_search,
    ),
    News(
        route = "news",
        label = "News",
        iconResId = R.drawable.ic_news,
    ),
    Portfolio(
        route = "portfolio",
        label = "Portfolio",
        iconResId = R.drawable.ic_portfolio,
    ),
}
