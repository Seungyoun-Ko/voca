package com.koksy.appinvest.data.repository

import com.koksy.appinvest.domain.model.MarketScannerHighlight
import com.koksy.appinvest.domain.model.RankingCategory
import com.koksy.appinvest.domain.model.StockRankingItem
import com.koksy.appinvest.domain.model.ThemeRankingItem
import com.koksy.appinvest.domain.repository.RankingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class DummyRankingRepository : RankingRepository {
    override fun observeStockRankings(category: RankingCategory): Flow<List<StockRankingItem>> {
        return flowOf(
            when (category) {
                RankingCategory.UPSIDE -> upsideRankings
                RankingCategory.OVERVALUED -> overvaluedRankings
                RankingCategory.FIFTY_TWO_WEEK_HIGH -> fiftyTwoWeekHighRankings
                RankingCategory.FIFTY_TWO_WEEK_LOW -> fiftyTwoWeekLowRankings
                RankingCategory.TRADING_VALUE -> tradingValueRankings
                RankingCategory.GAINERS -> gainerRankings
                RankingCategory.LOSERS -> loserRankings
                RankingCategory.THEMES -> emptyList()
            },
        )
    }

    override fun observeThemeRankings(): Flow<List<ThemeRankingItem>> {
        return flowOf(themeRankings)
    }

    override fun observeMarketScannerHighlights(): Flow<List<MarketScannerHighlight>> {
        return flowOf(
            listOf(
                upsideRankings.first().toHighlight(RankingCategory.UPSIDE),
                overvaluedRankings.first().toHighlight(RankingCategory.OVERVALUED),
                fiftyTwoWeekHighRankings.first().toHighlight(RankingCategory.FIFTY_TWO_WEEK_HIGH),
                fiftyTwoWeekLowRankings.first().toHighlight(RankingCategory.FIFTY_TWO_WEEK_LOW),
                tradingValueRankings.first().toHighlight(RankingCategory.TRADING_VALUE),
                gainerRankings.first().toHighlight(RankingCategory.GAINERS),
                loserRankings.first().toHighlight(RankingCategory.LOSERS),
            ),
        )
    }

    private val upsideRankings = listOf(
        stockItem(
            rank = 1,
            symbol = "005930",
            name = "삼성전자",
            market = "KOSPI",
            price = "78,400원",
            changeRate = 1.17,
            tradingValue = "1.9조원",
            metricLabel = "상승여력",
            metricValue = "+42%",
            tags = listOf("메모리", "HBM", "저평가"),
        ),
        stockItem(
            rank = 2,
            symbol = "SCHD",
            name = "Schwab US Dividend Equity ETF",
            market = "NYSE Arca",
            price = "$79.12",
            changeRate = 0.42,
            tradingValue = "$612M",
            metricLabel = "상승여력",
            metricValue = "+31%",
            tags = listOf("배당", "ETF", "현금흐름"),
        ),
        stockItem(
            rank = 3,
            symbol = "VRT",
            name = "Vertiv",
            market = "NYSE",
            price = "$91.44",
            changeRate = 2.38,
            tradingValue = "$1.8B",
            metricLabel = "상승여력",
            metricValue = "+27%",
            tags = listOf("데이터센터", "전력"),
        ),
        stockItem(
            rank = 4,
            symbol = "034020",
            name = "두산에너빌리티",
            market = "KOSPI",
            price = "21,350원",
            changeRate = 6.21,
            tradingValue = "7,420억원",
            metricLabel = "상승여력",
            metricValue = "+24%",
            tags = listOf("SMR", "전력인프라"),
        ),
    )

    private val overvaluedRankings = listOf(
        stockItem(
            rank = 1,
            symbol = "PLTR",
            name = "Palantir",
            market = "NYSE",
            price = "$122.80",
            changeRate = -1.12,
            tradingValue = "$5.9B",
            metricLabel = "하락위험",
            metricValue = "-38%",
            tags = listOf("AI 소프트웨어", "고PER"),
        ),
        stockItem(
            rank = 2,
            symbol = "TSLA",
            name = "Tesla",
            market = "NASDAQ",
            price = "$183.92",
            changeRate = -0.72,
            tradingValue = "$18.6B",
            metricLabel = "하락위험",
            metricValue = "-29%",
            tags = listOf("전기차", "로보택시"),
        ),
        stockItem(
            rank = 3,
            symbol = "IONQ",
            name = "IonQ",
            market = "NYSE",
            price = "$41.21",
            changeRate = 8.73,
            tradingValue = "$920M",
            metricLabel = "하락위험",
            metricValue = "-26%",
            tags = listOf("양자컴퓨터", "테마 과열"),
        ),
        stockItem(
            rank = 4,
            symbol = "ARKK",
            name = "ARK Innovation ETF",
            market = "ETF",
            price = "$44.36",
            changeRate = -3.02,
            tradingValue = "$486M",
            metricLabel = "하락위험",
            metricValue = "-18%",
            tags = listOf("혁신성장", "ETF"),
        ),
    )

    private val fiftyTwoWeekHighRankings = listOf(
        stockItem(
            rank = 1,
            symbol = "NVDA",
            name = "NVIDIA",
            market = "NASDAQ",
            price = "$1,142.32",
            changeRate = 2.84,
            tradingValue = "$42.1B",
            metricLabel = "52주 고가 근접",
            metricValue = "99.8%",
            tags = listOf("AI 반도체", "데이터센터"),
        ),
        stockItem(
            rank = 2,
            symbol = "AVGO",
            name = "Broadcom",
            market = "NASDAQ",
            price = "$1,805.44",
            changeRate = 1.96,
            tradingValue = "$6.2B",
            metricLabel = "52주 고가 근접",
            metricValue = "99.1%",
            tags = listOf("AI 네트워크", "반도체"),
        ),
        stockItem(
            rank = 3,
            symbol = "CEG",
            name = "Constellation Energy",
            market = "NASDAQ",
            price = "$302.18",
            changeRate = 3.12,
            tradingValue = "$1.3B",
            metricLabel = "52주 고가 근접",
            metricValue = "98.6%",
            tags = listOf("원전", "전력"),
        ),
        stockItem(
            rank = 4,
            symbol = "VRT",
            name = "Vertiv",
            market = "NYSE",
            price = "$91.44",
            changeRate = 2.38,
            tradingValue = "$1.8B",
            metricLabel = "52주 고가 근접",
            metricValue = "97.9%",
            tags = listOf("데이터센터", "냉각"),
        ),
    )

    private val fiftyTwoWeekLowRankings = listOf(
        stockItem(
            rank = 1,
            symbol = "RIVN",
            name = "Rivian",
            market = "NASDAQ",
            price = "$10.14",
            changeRate = -7.91,
            tradingValue = "$1.1B",
            metricLabel = "52주 저가 근접",
            metricValue = "1.4% 위",
            tags = listOf("전기차", "신저가"),
        ),
        stockItem(
            rank = 2,
            symbol = "096770",
            name = "SK이노베이션",
            market = "KOSPI",
            price = "104,200원",
            changeRate = -4.38,
            tradingValue = "2,180억원",
            metricLabel = "52주 저가 근접",
            metricValue = "2.1% 위",
            tags = listOf("2차전지", "정유"),
        ),
        stockItem(
            rank = 3,
            symbol = "PYPL",
            name = "PayPal",
            market = "NASDAQ",
            price = "$59.26",
            changeRate = -2.15,
            tradingValue = "$1.6B",
            metricLabel = "52주 저가 근접",
            metricValue = "3.0% 위",
            tags = listOf("핀테크", "결제"),
        ),
        stockItem(
            rank = 4,
            symbol = "BABA",
            name = "Alibaba",
            market = "NYSE",
            price = "$76.84",
            changeRate = -1.48,
            tradingValue = "$2.0B",
            metricLabel = "52주 저가 근접",
            metricValue = "3.8% 위",
            tags = listOf("중국 소비", "플랫폼"),
        ),
    )

    private val tradingValueRankings = listOf(
        stockItem(
            rank = 1,
            symbol = "NVDA",
            name = "NVIDIA",
            market = "NASDAQ",
            price = "$1,142.32",
            changeRate = 2.84,
            tradingValue = "$42.1B",
            metricLabel = "거래대금",
            metricValue = "$42.1B",
            tags = listOf("AI 반도체", "데이터센터"),
        ),
        stockItem(
            rank = 2,
            symbol = "TSLA",
            name = "Tesla",
            market = "NASDAQ",
            price = "$183.92",
            changeRate = -0.72,
            tradingValue = "$18.6B",
            metricLabel = "거래대금",
            metricValue = "$18.6B",
            tags = listOf("전기차", "로보택시"),
        ),
        stockItem(
            rank = 3,
            symbol = "005930",
            name = "삼성전자",
            market = "KOSPI",
            price = "78,400원",
            changeRate = 1.17,
            tradingValue = "1.9조원",
            metricLabel = "거래대금",
            metricValue = "1.9조원",
            tags = listOf("메모리", "온디바이스 AI"),
        ),
        stockItem(
            rank = 4,
            symbol = "TIGER 미국S&P500",
            name = "TIGER 미국S&P500",
            market = "ETF",
            price = "18,965원",
            changeRate = 0.31,
            tradingValue = "842억원",
            metricLabel = "거래대금",
            metricValue = "842억원",
            tags = listOf("미국지수", "ETF"),
        ),
    )

    private val gainerRankings = listOf(
        stockItem(
            rank = 1,
            symbol = "OKLO",
            name = "Oklo",
            market = "NYSE",
            price = "$14.88",
            changeRate = 12.46,
            tradingValue = "$1.4B",
            metricLabel = "당일 등락률",
            metricValue = "+12.46%",
            tags = listOf("SMR", "원전"),
        ),
        stockItem(
            rank = 2,
            symbol = "IONQ",
            name = "IonQ",
            market = "NYSE",
            price = "$41.21",
            changeRate = 8.73,
            tradingValue = "$920M",
            metricLabel = "당일 등락률",
            metricValue = "+8.73%",
            tags = listOf("양자컴퓨터"),
        ),
        stockItem(
            rank = 3,
            symbol = "034020",
            name = "두산에너빌리티",
            market = "KOSPI",
            price = "21,350원",
            changeRate = 6.21,
            tradingValue = "7,420억원",
            metricLabel = "당일 등락률",
            metricValue = "+6.21%",
            tags = listOf("SMR", "전력인프라"),
        ),
        stockItem(
            rank = 4,
            symbol = "VRT",
            name = "Vertiv",
            market = "NYSE",
            price = "$91.44",
            changeRate = 2.38,
            tradingValue = "$1.8B",
            metricLabel = "당일 등락률",
            metricValue = "+2.38%",
            tags = listOf("데이터센터", "전력"),
        ),
    )

    private val loserRankings = listOf(
        stockItem(
            rank = 1,
            symbol = "RIVN",
            name = "Rivian",
            market = "NASDAQ",
            price = "$10.14",
            changeRate = -7.91,
            tradingValue = "$1.1B",
            metricLabel = "당일 등락률",
            metricValue = "-7.91%",
            tags = listOf("전기차"),
        ),
        stockItem(
            rank = 2,
            symbol = "096770",
            name = "SK이노베이션",
            market = "KOSPI",
            price = "104,200원",
            changeRate = -4.38,
            tradingValue = "2,180억원",
            metricLabel = "당일 등락률",
            metricValue = "-4.38%",
            tags = listOf("2차전지", "정유"),
        ),
        stockItem(
            rank = 3,
            symbol = "ARKK",
            name = "ARK Innovation ETF",
            market = "ETF",
            price = "$44.36",
            changeRate = -3.02,
            tradingValue = "$486M",
            metricLabel = "당일 등락률",
            metricValue = "-3.02%",
            tags = listOf("혁신성장", "ETF"),
        ),
        stockItem(
            rank = 4,
            symbol = "PYPL",
            name = "PayPal",
            market = "NASDAQ",
            price = "$59.26",
            changeRate = -2.15,
            tradingValue = "$1.6B",
            metricLabel = "당일 등락률",
            metricValue = "-2.15%",
            tags = listOf("핀테크", "결제"),
        ),
    )

    private val themeRankings = listOf(
        ThemeRankingItem(
            rank = 1,
            name = "SMR",
            momentumScore = 92,
            changeRate = 5.84,
            leadingStocks = listOf("OKLO", "두산에너빌리티", "BWXT"),
            summary = "전력 수요와 원전 정책 모멘텀이 함께 반영되는 중",
        ),
        ThemeRankingItem(
            rank = 2,
            name = "데이터센터",
            momentumScore = 87,
            changeRate = 3.76,
            leadingStocks = listOf("NVDA", "VRT", "EQIX"),
            summary = "AI 서버 증설과 전력/냉각 인프라 수혜가 지속",
        ),
        ThemeRankingItem(
            rank = 3,
            name = "양자컴퓨터",
            momentumScore = 81,
            changeRate = 2.94,
            leadingStocks = listOf("IONQ", "RGTI", "QBTS"),
            summary = "상용화 기대와 정부 연구 예산 확대 이슈가 부각",
        ),
        ThemeRankingItem(
            rank = 4,
            name = "배당성장 ETF",
            momentumScore = 74,
            changeRate = 1.22,
            leadingStocks = listOf("SCHD", "VIG", "TIGER 미국배당다우존스"),
            summary = "금리 인하 기대 속 현금흐름형 자산 선호 회복",
        ),
    )

    private fun stockItem(
        rank: Int,
        symbol: String,
        name: String,
        market: String,
        price: String,
        changeRate: Double,
        tradingValue: String,
        metricLabel: String,
        metricValue: String,
        tags: List<String>,
    ): StockRankingItem {
        return StockRankingItem(
            rank = rank,
            symbol = symbol,
            name = name,
            market = market,
            price = price,
            changeRate = changeRate,
            tradingValue = tradingValue,
            metricLabel = metricLabel,
            metricValue = metricValue,
            themeTags = tags,
        )
    }

    private fun StockRankingItem.toHighlight(category: RankingCategory): MarketScannerHighlight {
        return MarketScannerHighlight(
            category = category,
            symbol = symbol,
            name = name,
            market = market,
            metricLabel = metricLabel,
            metricValue = metricValue.ifBlank { tradingValue },
            changeRate = changeRate,
        )
    }
}
