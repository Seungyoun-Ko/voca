package com.koksy.appinvest.domain.model

enum class RankingCategory(
    val label: String,
    val description: String,
) {
    UPSIDE(
        label = "상승여력",
        description = "적정가치 대비 상승 여력이 큰 종목",
    ),
    OVERVALUED(
        label = "고평가",
        description = "적정가치보다 높게 거래되는 종목",
    ),
    FIFTY_TWO_WEEK_HIGH(
        label = "52주 신고가",
        description = "최근 1년 최고가에 근접하거나 경신한 종목",
    ),
    FIFTY_TWO_WEEK_LOW(
        label = "52주 신저가",
        description = "최근 1년 최저가에 근접하거나 경신한 종목",
    ),
    TRADING_VALUE(
        label = "최대 거래",
        description = "거래대금과 거래량이 큰 시장 주도 종목",
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
