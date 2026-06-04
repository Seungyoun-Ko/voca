package com.koksy.appinvest.domain.model

enum class ComparisonMetricGroup(
    val label: String,
    val description: String,
) {
    BASIC(
        label = "기본",
        description = "시가총액, 거래대금, PER, PBR, PSR",
    ),
    CORE_FINANCIALS(
        label = "실적",
        description = "매출 성장률, 영업이익률, ROE, EPS 성장률",
    ),
    STABILITY(
        label = "안정성",
        description = "부채비율, 유동비율, 이자보상배율",
    ),
    CASH_FLOW(
        label = "현금흐름",
        description = "영업현금흐름, FCF, FCF Yield",
    ),
    DIVIDEND_ETF(
        label = "배당/ETF",
        description = "배당수익률, 배당성장률, 보수율, AUM, 추적오차",
    ),
}

data class MetricComparisonSet(
    val selectedSymbol: String,
    val peerGroupLabel: String,
    val group: ComparisonMetricGroup,
    val rows: List<MetricComparisonRow>,
)

data class MetricComparisonRow(
    val metricName: String,
    val helper: String,
    val higherIsBetter: Boolean,
    val values: List<MetricComparisonValue>,
)

data class MetricComparisonValue(
    val symbol: String,
    val displayName: String,
    val displayValue: String,
    val numericValue: Double?,
)
