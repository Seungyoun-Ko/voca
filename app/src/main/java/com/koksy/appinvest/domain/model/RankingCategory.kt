package com.koksy.appinvest.domain.model

enum class RankingCategory(
    val label: String,
    val description: String,
) {
    TRADING_VALUE(
        label = "거래대금",
        description = "국내외 주요 종목의 거래대금 상위",
    ),
    GAINERS(
        label = "급등",
        description = "당일 상승률 상위 종목",
    ),
    LOSERS(
        label = "급락",
        description = "당일 하락률 상위 종목",
    ),
    THEMES(
        label = "테마",
        description = "자체 테마별 시장 강도",
    ),
}
